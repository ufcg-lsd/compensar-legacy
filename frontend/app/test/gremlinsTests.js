buttonClicker = () => {
    // Now let's define our submission gremlin
    var targetElement, availableButtons;
    availableButtons = document.querySelectorAll('button, a');
    targetElement = availableButtons[Math.floor(Math.random()*availableButtons.length)]; // Then let's grab a random element from those results

    // Now, we create a dummy submission event
    var evt = document.createEvent('HTMLEvents');  // Create an event container
    evt.initEvent('click');  // Define our event as a submit event
    targetElement.dispatchEvent(evt);  // Finally, dispatch the submit event onto our randomly selected form

    // We also want to make sure to log this event like others gremlins do!      
    console.log('gremlin click button ', targetElement);
}

function initTests() {
    gremlins.createHorde().allGremlins()
    .gremlin(buttonClicker)
    .unleash();
}