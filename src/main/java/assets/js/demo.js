
'use strict';

$(document).ready(function() {
  var audio = $('.audio').get(0),
    textArea = $('#textArea');

  $('.audio').on('error', function () {
    alert("there was an error");
  });

  $('.audio').on('loadeddata', function () {
      console.log("let's go!");
  });

  $('.speak-button').click(function() {
    audio.pause();

    $('#textArea').focus();
    audio.setAttribute('src','speak?&' + $('form').serialize());
  });
});
