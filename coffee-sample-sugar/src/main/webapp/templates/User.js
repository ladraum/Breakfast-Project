/**
 * User.js - Extension of User.xhtml
 * @author Miere Liniel Teixeira
 */

/**
 * Dispatched on Application Load event
 */
	function onApplicationLoad() {
		application.addValidator(new DaSilvaValidator());
		application.branchesTree.event("onselectchild", updateBranchLabels);
		application.branchesTree.event("onunselectchild", updateBranchLabels);
	}

/**
 * Dispatched on dependents' grid remove button click
 * @param data
 * @param index
 */
	function onDependentsRemoveClick(data, index) {
		if (confirm( "Desejas remover o dependente '" + data.name + "'?" ))
			application.dependents.removeRow(index);
	}

/**
 * Clean the dependents' form
 */
	function cleanDependentsForm() {
		application.depName.setValue ("");
		application.depPhone.setValue ("");
		application.depIndex.setValue ("");
	}

/**
 * Dispatched on dependents' grid edit button click
 * @param data
 * @param index
 */
	function onDependentsEditClick(data, index) {
		application.depName.setValue (data.name);
		application.depPhone.setValue (data.phone);
		application.depIndex.setValue (index);
		application.dependentsDialog.show();
		application.depName.focus();
	}

/**
 * Dispatched on dependents' grid newDependent button click
 */
	function onNewDependentsBtnClick() {
		application.dependentsDialog.show();
		application.depName.focus();
	}

/**
 * Dispatched on dependents' dialog save button click
 */
	function onDependentsDialogSaveClick() {
		var rowData = {
				name: application.depName.getValue(),
				phone: application.depPhone.getValue()
			};

		var index = application.depIndex.getValue();
		if (index  != "")
			application.dependents.updateRow ( index, rowData );
		else
			application.dependents.addRow ( rowData );
		application.dependentsDialog.hide();
		cleanDependentsForm();
	}

/**
 * Dispatched on save button click
 */
	function onSaveClick() {
		application.submit();
	}

/**
 * Retrieves the selected branches' label from branches tree. The
 * returned value is comma separated.
 */
	function getSelectedBranches() {
		var tree = application.branchesTree;
		var selectedBranchesId = tree.getSelectedChildren();
		var labelBuffer = "";
		
		for (var i=0; i<selectedBranchesId.size(); i++) {
			var child = tree.getChild(selectedBranchesId[i]);
			if (i > 0)
				labelBuffer+= ", ";
			labelBuffer+= child.getLabel();
		}
		
		return labelBuffer;
	}

	function updateBranchLabels() {
		if (application.branchesTree.getSelectedChildren().size() > 2) {
			alert("Por favor, selecione apenas 2 unidades.");
			return;
		}

		application.selectedBranches
			.setValue ( getSelectedBranches() );
	}

	function onSelectedBranchesFocus() {
		application.branchDialog.show();
	}

/**
 * The Amazing "Da Silva" validator
 */
	DaSilvaValidator = Class(Validator);
	DaSilvaValidator.prototype.validate = function (fields) {
		if ( application.name.getValue().contains("da Silva")
		&&   application.motherName.getValue() == "") {
			this.setErrorMessage("Seu nome é muito comum, favor preencher o nome da mãe.");
			application.motherName.focus();
			return false;
		}
		return true;
	};
