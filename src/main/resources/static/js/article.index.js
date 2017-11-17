$(function () {
    var menuBtn = $('#btn-show-categories');

    var nav = $('.nav-primary-categories');
    var cover = $('.cover-nav-primary-categories');

    nav.find('.nav-link').on('click', function () {
        var link = $(this);
        var categories = $('.item-primary-category');

        var id = link.data('category-id');
        if (id === 'all') {
            categories.show();
        } else {
            categories.hide();
            categories.filter('[data-category-id="' + id + '"]')
                .show();
        }
        cover.trigger('click');
    });

    cover.on('click', function () {
        cover.hide();

        nav.animate({left: '-200px'}, 200, function () {
            nav.hide();
        });
    });

    menuBtn.on('click', function () {
        cover.show();

        nav.show().animate({left: '0'}, 200);
    });

    cover.trigger('click');
});