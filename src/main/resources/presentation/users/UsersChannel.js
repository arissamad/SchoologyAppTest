function UsersChannel() {
	new PageHeaderWidget("Users");
	
	new ButtonWidget("Add User", $A(this, function() {
		new EditUser(null).setRefreshHandler($A(this, function() {
			tw.refresh();
		}));
	}));
	new LW(2);
	
	var tw = new TableWidget(this, {css: {width: "100%"}});
	
	tw.addHeader("Name", {css: {width: "300px"}});
	tw.addColumn(function(user) {
		new BoldLinkWidget(user.name, $A(this, function() {
			new ViewUser(user.id);
		}));
	});

	tw.addHeader("Email", {css: {width: "300px"}});
	tw.addColumn("email");
	
	tw.addHeader("Edit");
	tw.addColumn(function(user) {
		var td = current;
		
		new LinkWidget("Edit", $A(this, function() {
			new EditUser(user.id).setRefreshHandler($A(this, function(fullRefresh) {
				if(fullRefresh == true) tw.refresh();
				else tw.refreshRow(user.id, td);
			}));
		}));
	});
	
	tw.turnOnPaging();
	tw.turnOnSearching();
	tw.setLoaderResource("/api/users");
	
	tw.render();
}