$('document').ready(function() {
    $('.table .btn-warning').on('click',function(event) {
        event.preventDefault();
        var href = $(this).attr('href');

        $.get(href, function(takes, status) {
            $('#IDEdit').val(takes.ID);
            $('#course_idEdit').val(takes.course_id);
            $('#sec_idEdit').val(takes.sec_id);
            $('#semesterEdit').val(takes.semester);
            $('#yearEdit').val(takes.year);
            $('#component_gradeEdit').val(takes.component_grade);
            $('#final_exam_gradeEdit').val(takes.final_exam_grade);
            $('#editModal').modal('show');
        });
    });

    $('#editModal').on('shown.bs.modal', function() {
        $('#saveButton').on('click', function() {
            var updatedID = $('#IDEdit').val();
            var updatedCourseID = $('#course_idEdit').val();
            var updatedSecID = $('#sec_idEdit').val();
            var updatedSemester = $('#semesterEdit').val();
            var updatedYear = $('#yearEdit').val();
            var updatedComponentGrade = $('#component_gradeEdit').val();
            var updatedFinalExamGrade = $('#final_exam_gradeEdit').val();

            $('#editModal').modal('hide');
        });
    });
});