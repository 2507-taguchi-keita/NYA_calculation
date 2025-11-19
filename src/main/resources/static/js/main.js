$(document).ready(function () {

        const flash = $("#flash-message");

        if (flash.length) {
            flash.fadeIn(200);

            setTimeout(() => {
                flash.fadeOut(500);
            }, 2000);
        }

});