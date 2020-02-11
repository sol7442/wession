var wession = function($){
	var tmp = null,
			m_currentPoilcy = "PERMISSION";
	return {
		removeRows: function(rows, callback) {
			// remove rows code..
			var len = rows.length, // length object of array
					i = 0,
					idx = []; 
			for (;i < len; i++) {
				idx.push(rows[i].id);
			}
			// selected identity key (eg. id)
			if (idx.length === 0) {
				alert("삭제할 데이터를 선택하세요");
			} else {
				if (typeof callback === 'function') {
					callback(idx.join(','));
				}
			}
		}, // removeRows function 
		getCurrentPolicy: function(){
			return m_currentPoilcy;
		},
		setCurrentPolicy: function(policy, callback) {
			m_currentPoilcy = policy.toUpperCase();
			if (typeof callback === "function") {
				callback(m_currentPoilcy);
			}
		}
	}
}(jQuery);

	// global code
$(".accordion-toggle").on("click", function(e){
		//$(this).children('.icon-caret-right').switchClass("icon-caret-right", "icon-caret-down", 100);
		//$(this).children('.icon-caret-down').switchClass("icon-caret-down", "icon-caret-right", 200);
});

	// metisMenu 
$(".sidebar-nav ul ul a").hover(function(){
	$(this).parent("a").toggleClass("active");
});