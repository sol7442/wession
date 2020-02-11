<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="modal fade" id="modalPassword" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-body">
      		<form id="password-mod" class="form-horizontal" role="form">
      			<fieldset>
      				<legend>비밀번호 변경</legend>
      				<div class="form-group">
      						<label for="cur_passwd" class="col-lg-3 control-label">현재 비밀번호</label>
      						<div class="col-lg-9">
      						<input type="password" class="form-control" id="cur_passwd" name="cur_passwd" />
      						</div>
      				</div>
      				<hr />
      				<div class="form-group">
      						<label for="new_passwd" class="col-lg-3 control-label">신규 비밀번호</label>
      						<div class="col-lg-9">
      						<input type="password" class="form-control" id="new_passwd" name="new_passwd" />
      						</div>
      				</div>
      				<div class="form-group">
      						<label for="new_passwd_confirm" class="col-lg-3 control-label">비밀번호 확인</label>
      						<div class="col-lg-9">
      						<input type="password" class="form-control" id="new_passwd_confirm" name="new_passwd_confirm" />
      						</div>
      				</div>
      			</fieldset>

      		</form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-danger" id="submit-passwd">Save changes</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<script>
	// 비밀번호 변경 
	$("#submit-passwd").on('click', function(){
		var frm = $("#password-mod"),
				cur_passwd = $("#cur_passwd"),
				new_passwd = $("#new_passwd"),
				new_passwd_confirm = $("#new_passwd_confirm");
		
		if ($.trim(cur_passwd.val()).length === 0) {
			alert("현재 비밀번호를 입력하세요");
			cur_passwd.focus();
			return;
		}
		if ($.trim(new_passwd.val()).length === 0) {
			alert("신규 비밀번호를 입력하세요");
			new_passwd.focus();
			return;
		}
		if (new_passwd.val() != new_passwd_confirm.val()) {
			alert("비밀번호를 확인하세요");
			return;
		}
		
		$.post("../../json/self_passwordchange.jsp", $(frm).serialize(), function(resp) {
			var json = $.parseJSON(resp);
			alert(json.message);
		});

});
</script>