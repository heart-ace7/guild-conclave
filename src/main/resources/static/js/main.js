;

$.fn.extend({
    markdownEditor: function () {
        var textarea = this;

        var createToolbarElement = function (styleClass, clickEventHandler) {
            var btn = $('<a/>').attr({
                'href': 'javascript:void(0);'
            }).addClass(styleClass);

            btn.on('click', clickEventHandler);

            return btn;
        };
        var modals = {
            link: (function () {
                var modal = $('#modal-md-link');

                var textLinkInput = modal.find('input[name="text"]');
                var urlInput = modal.find('input[name="url"]');
                var submitBtn = modal.find('.btn-primary');

                submitBtn.on('click', function () {
                    modal.modal('hide');

                    textarea.insertTextAtCaret(
                        '[' + textLinkInput.val() + '](' + urlInput.val() + ')', '');
                });
                modal.on('show.bs.modal', function () {
                    textLinkInput.val('');
                    urlInput.val('');
                });
                return modal;
            })(),
            image: (function () {
                var modal = $('#modal-md-image');

                var imageInput = modal.find('input[name="imageFile"]');
                var imagePreview = $('#img-upload-preview');
                var submitBtn = modal.find('.btn-primary');
                var form = modal.find('form');

                var panelViews = $('#stateful-panel-views');

                panelViews.on('show.panel', function (event, status) {
                    var panels = $(this).find('.panel-view');

                    panels.hide();
                    panels.filter('[data-status="' + status + '"]').show();
                });

                form.on('submit', function () {
                    var formData = new FormData(this);

                    $.ajax({
                        url: $(this).attr('action'),
                        type: $(this).attr('method'),
                        data: formData,
                        async: true,
                        cache: false,
                        contentType: false,
                        processData: false
                    }).done(function (response) {
                        imagePreview.attr('src', response.imagePath);

                        panelViews.trigger('show.panel', 'preview');
                    });
                    return false;
                });
                imageInput.on('change', function () {
                    panelViews.trigger('show.panel', 'uploading');

                    form.submit();
                });
                submitBtn.on('click', function () {
                    var text = '画像';
                    var url = imagePreview.attr('src');

                    modal.modal('hide');

                    textarea.insertTextAtCaret(
                        '![' + text + '](' + url + ')', '');
                });
                modal.on('show.bs.modal', function () {
                    imageInput.val('');
                    imagePreview.attr('src', '');

                    panelViews.trigger('show.panel', 'input');
                });
                return modal;
            })()
        };
        var tools = {
            bold: createToolbarElement('fa fa-bold', function () {
                textarea.insertTextAtCaret('**', '**');
            }),
            italic: createToolbarElement('fa fa-italic', function () {
                textarea.insertTextAtCaret('*', '*');
            }),
            strike: createToolbarElement('fa fa-strikethrough', function () {
                textarea.insertTextAtCaret('~~', '~~');
            }),
            header: createToolbarElement('fa fa-header', function () {
                textarea.insertTextAtLineHead('# ');
            }),
            ul: createToolbarElement('fa fa-list-ul', function () {
                textarea.insertTextAtLineHead('- ');
            }),
            ol: createToolbarElement('fa fa-list-ol', function () {
                textarea.insertTextAtLineHead('1. ');
            }),
            link: createToolbarElement('fa fa-link', function () {
                modals.link.modal('show');
            }),
            image: createToolbarElement('fa fa-picture-o', function () {
                modals.image.modal('show');
            }),
            table: createToolbarElement('fa fa-table', function () {
                textarea.insertTextAtCaret('',
                    '\n\n' +
                    '| head1 | head2 | head3 |\n' +
                    '|:------|:------|:------|\n' +
                    '| col 1 | col 2 | col 3 |\n' +
                    '\n'
                );
            }),
            navigation: createToolbarElement('fa fa-question-circle', function () {
                // TODO
            })
        };
        var separator = function () {
            return $('<i/>').addClass('separator').text('|');
        };

        var editorToolbar = $('<div/>').addClass('editor-toolbar').append(
            tools.bold, tools.italic, tools.strike, tools.header, separator(),
            tools.ul, tools.ol, separator(),
            tools.link, tools.image, tools.table);
        return textarea.before(editorToolbar).attr('style', 'border-radius: 0 0 3px 3px');
    },
    /**
     * insert text on textarea.
     * --------------------------------
     * - no IE support
     *
     * @param text1 after text
     * @param text2 before text
     */
    insertTextAtCaret: function (text1, text2) {
        var textarea = this.get(0);

        var scrollTop = textarea.scrollTop;
        var positionStart = textarea.selectionStart;
        var positionEnd = textarea.selectionEnd;

        var front = (textarea.value).substring(0, positionStart);
        var selected = (textarea.value).substring(positionStart, positionEnd);
        var back = (textarea.value).substring(positionEnd, textarea.value.length);

        textarea.value = (front + text1 + selected + text2 + back);

        // reset selected position
        textarea.selectionStart = positionStart + text1.length;
        textarea.selectionEnd = positionEnd + text1.length;
        textarea.focus();

        textarea.scrollTop = scrollTop;
    },
    insertTextAtLineHead: function (text) {
        var textarea = this.get(0);

        var scrollTop = textarea.scrollTop;
        var start = textarea.selectionStart;
        var end = textarea.selectionEnd;

        var front = (textarea.value).substring(0, start);
        var back = (textarea.value).substring(start, textarea.value.length);

        var lines = front.split('\n');
        var lastIndex = lines.length - 1;

        if (!lines[lastIndex].startsWith(text)) {
            lines[lastIndex] = text + lines[lastIndex];

            textarea.value = (lines.join('\n') + back);

            // reset selected position
            textarea.selectionStart = start + text.length;
            textarea.selectionEnd = end + text.length;
            textarea.scrollTop = scrollTop;
        }
        textarea.focus();
    }
});