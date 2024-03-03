$(document).ready(function(){

	$(".suite").click(function(){
    $("html,body").animate({scrollTop: $("#body").offset().top}, 1000, "easeInOutQuint")
    })
    
    $(window).scroll( function(){
        console.log( $(this).scrollTop());
        var yPos = $(window).scrollTop()/2;
        $("#header").css("background-position", "left "+yPos+"px");
    })
})