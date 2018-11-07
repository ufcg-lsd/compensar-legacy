// When the user scrolls down 80px from the top of the document, resize the navbar's padding and the logo's font size
window.onscroll = function() {scrollFunction()};

function scrollFunction() {
    const navbar =  document.getElementById("navbar");
    const logo =  document.getElementById("logo");


  if (document.body.scrollTop > 80 || document.documentElement.scrollTop > 80) {
    navbar.style.padding = "30px 10px";
    logo.style.fontSize = "25px";
  } else {
    navbar.style.padding = "80px 10px";
    logo.style.fontSize = "35px";
  }
}