function isOrContains(container, node) {
    while (node) {
        if (node === container) {
            return true;
        }
        node = node.parentNode;
    }
    return false;
}

function preprocess(node) {
    if (node.nodeType === Node.TEXT_NODE && !node.parentNode.classList.contains('textNode')) {
        let parentNode = node.parentNode;
        let spanNode = document.createElement("span");
        parentNode.insertBefore(spanNode, node);
        spanNode.appendChild(node)
        spanNode.classList.add("textNode");
    }
}

function isEqualClasslist(node1, node2) {
    if (!node1 || !node2) return false;
    let arr1 = node1.classList;
    let arr2 = node2.classList;
    if (arr1 === null || arr2 === null) return false;
    if (arr1.length != arr2.length) return false;
    for(let x of arr1) {
        if (!arr2.contains(x)) return false;
    }
    return true;
}

function mergeText(text1, text2, result) {
    if (result === 2) {
        text2.childNodes[0].insertData(0, text1.childNodes[0].wholeText);
        text1.remove();
    } else {
        text1.childNodes[0].appendData(text2.childNodes[0].wholeText);
        text2.remove();
    }
}

function highlightElement(node, type) {
    if (node.classList.contains("textNode")) {
        if (type) {
            node.classList.add("highlighted");
        } else {
            node.classList.remove("highlighted");
        }
        if (isEqualClasslist(node.previousSibling, node)) {
            mergeText(node.previousSibling, node, 2);
        }
        if (isEqualClasslist(node.nextSibling, node)) {
            mergeText(node, node.nextSibling, 1);
        }
    } else {
        let childs = node.childNodes;
        for(let child of childs) {
            preprocess(child);
        }
        if (childs.length === 0) {
            if (type) {
                node.classList.add("highlighted");
            } else {
                node.classList.remove("highlighted");
            }
        } else {
            let child = node.childNodes[0];
            while(child !== null) {
                highlightElement(child, type);
                child = child.nextSibling;
            }
        }
    }
}

function highligthText(node, start, end, type) {
    preprocess(node);
    let parentNode = node.parentNode;
    
    let s = node.data;

    var part1 = s.substring(0, start);
    var part2 = s.substring(start, end);
    var part3 = s.substring(end);
    if (part1.length > 0) {
        let previousNode = parentNode.cloneNode(true);
        previousNode.childNodes[0].data = part1;
        parentNode.parentNode.insertBefore(previousNode, parentNode);
    }
    if (part3.length > 0) {
        let nextNode = parentNode.cloneNode(true);
        nextNode.childNodes[0].data = part3;
        parentNode.parentNode.insertBefore(nextNode, parentNode.nextSibling);
    }
    node.data = part2;

    if (type) {
        parentNode.classList.add("highlighted");
    } else {
        parentNode.classList.remove("highlighted");
    }

    if (isEqualClasslist(parentNode.previousSibling, parentNode)) {
        mergeText(parentNode.previousSibling, parentNode, 2);
    }
    if (isEqualClasslist(parentNode.nextSibling, parentNode)) {
        mergeText(parentNode, parentNode.nextSibling, 1);
    }
}

function highlight(type) {
    let range = window.getSelection().getRangeAt(0);
    let start = range.startContainer;
    let end = range.endContainer;
    if(range.startContainer === range.endContainer) {
        if(range.startContainer.nodeType === Node.TEXT_NODE) {
            highligthText(range.startContainer, range.startOffset, range.endOffset, type);
        } else if (range.startContainer.childNodes.length){
            highlightElement(range.startContainer, type);
        }
    } else {
        let el = range.startContainer;
        while(el.nextSibling === null) {
            el = el.parentNode;
        }
        el = el.nextSibling;
        while(el != range.endContainer) {
            if(isOrContains(el, range.endContainer)) {
                if(el === range.endContainer) break;
                el = el.childNodes[0];
            } else {
                if (el.nodeType === Node.TEXT_NODE) {
                    highligthText(el, 0, el.length, type);
                } else {
                    highlightElement(el, type);
                }
                if (range.endContainer !== end) break;
                while(el.nextSibling === null) {
                    el = el.parentNode;
                }
                el = el.nextSibling;
            }
        }
        if(range.startContainer === start && start.nodeType === Node.TEXT_NODE) {
            highligthText(start, range.startOffset, start.length, type);
        } else if (range.startContainer === start) {
            highlightElement(start, type);
        }
        if(range.endContainer === end && end.nodeType === Node.TEXT_NODE) {
            highligthText(end, 0, range.endOffset, type);
        } else if (range.endContainer === end) {
            highlightElement(end, type);
        }
    }
    window.getSelection().removeAllRanges();
}