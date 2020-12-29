$(document).ready(function () {
    // Init
    $('.image-section').hide();
    $('.loader').hide();
    $('#result').hide();

    // Upload Preview
    function readURL(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            reader.onload = function (e) {
                $('#imagePreview').css('background-image', 'url(' + e.target.result + ')');
                $('#imagePreview').hide();
                $('#imagePreview').fadeIn(650);
            }
            reader.readAsDataURL(input.files[0]);
        }
    }
    $("#imageUpload").change(function () {
        $('.image-section').show();
        $('#btn-predict').show();
        $('#result').text('');
        $('#result').hide();
        readURL(this);
    });

    // Predict
    $('#btn-predict').click(function () {
        var form_data = new FormData($('#upload-file')[0]);

        // Show loading animation
        $(this).hide();
        $('.loader').show();
        var categ=$(this).val();
        // Make prediction by calling api /predict
        $.ajax({
            type: 'POST',
            url:'/'+ categ + '/predict',
            data: form_data,
            contentType: false,
            cache: false,
            processData: false,
            async: true,
            success: function (data) {
                // Get and display the result
                $('.loader').hide();
              //  $('#result').fadeIn(600);
                //$('#result').text(' Result:  ' + data);
                var header = $('#results thead');
       var body = $('#results tbody');
       var hTr;
       $('#results thead').append(hTr = $('<tr>'));
       // Headers
       var keyList = Object.keys(data);
       for (var h = 0; h < keyList.length; h++) {
          hTr.append($('<td>', { text: keyList[h] }))
       }
       // Body
       for (var h = 0; h < keyList.length; h++) {
          var d = data[keyList[h]];
          $('#results-rows').append($('<td>', { text: d }));
            
       }
                console.log('Success!');
            },
        });
    });

});
