//Ao rolar a página para baixo diminui tamanho da navbar

window.onscroll = function() {scrollFunction()};

function scrollFunction() {
    const navbar =  document.getElementById("navbar");


  if (document.body.scrollTop > 80 || document.documentElement.scrollTop > 80) {
    navbar.style.padding = "20px 10px";
  } else {
    navbar.style.padding = "50px 10px";
  }
}

//Desliza icones automaticamente sem desordenar

$('#carouselExample').on('slide.bs.carousel', function (e) {

  var $e = $(e.relatedTarget);
  var idx = $e.index();
  var itemsPerSlide = 4;
  var totalItems = $('.carousel-item').length;
  
  if (idx >= totalItems-(itemsPerSlide-1)) {
      var it = itemsPerSlide - (totalItems - idx);
      for (var i=0; i<it; i++) {
          // append slides to end
          if (e.direction=="left") {
              $('.carousel-item').eq(i).appendTo('.carousel-inner');
          }
          else {
              $('.carousel-item').eq(0).appendTo('.carousel-inner');
          }
      }
  }
});