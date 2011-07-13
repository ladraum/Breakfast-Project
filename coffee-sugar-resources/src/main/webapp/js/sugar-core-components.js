/*
 * Copyright 2011 Miere Liniel Teixeira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var MSG_INVALID_ARGUMENT_LIST = "Invalid arguments list. Components excepts at least \"{id: 'someId'}\" as argument.";
var MSG_INVALID_ID_ARGUMENT   = "Invalid 'id' argument: Unknown object.";
var MSG_INVALID_ARGUMENT      = "Invalid argument: ";
var MSG_NOT_IMPLEMENTED_YET   = "Not implemented yet: ";

var VALID_WIDGET_DOM_EVENTS = ["blur", "change", "click",
		"dblclick", "focus", "mousedown", "mousemove",
		"mouseout", "mouseover", "mouseup", "keydown",
		"keypress", "keyup", "select"];

/* -------------------------------------------------------------------------
 * TextMask
 * ------------------------------------------------------------------------- */
	TextMask = {

        MASKS_FILTER: {
            '9':/\d/,
            'Z':/[a-zA-Z]/,
            '#':/./
        },

        MASKS_ALIASES: {
            date: "99/99/9999",
            date_us: "`[0-1]`9/`[0-3]`9/9999",
            date_br: "`[0-3]`9/`[0-1]`9/9999",
            phone: "99 9999-9999",
            cpf: "999.999.999-99"
        },
        
        register: function (alias, mask) {
        	TextMask.MASKS_ALIASES[alias] = mask;
        },

        applyRule: function (rule, value) {
            var re = (rule.indexOf("`") < 0) ? TextMask.MASKS_FILTER[rule]
                        : new RegExp("^" + rule.replace(/`/g,""));
            if (!re) return rule
            var matched = value.match(re);
            return !matched ? null : matched[0];
        },

        applyMask: function (value, mask) {
            mask = TextMask.MASKS_ALIASES[mask] || mask;
            var buffer = "";
            for (var i=0, j=0; i<mask.length && i<value.length;) {
                var k = (mask[j] == '`') ? mask.indexOf("`",j+1) + 1 : j + 1;
                var sstr = value.substring(i);
                var rule = mask.substring(j,k);
                var result = TextMask.applyRule(rule, sstr);
                if (!result)
                    return buffer;
                buffer+= result;
                i+= result.length;
                j = k;
            }
            return buffer;
        }

    };

/* -------------------------------------------------------------------------
 * Component
 * ------------------------------------------------------------------------- */
    Component = Class();
    Component.prototype.toString = function () { return "Generic Component Object"; };

    Component.prototype.constructor = function (args) {
        if (!args)
            throw MSG_INVALID_ARGUMENT_LIST;
        this.private("label",  args.label || "");
        this.private("children", new Array());
        this.private("id",     args.id);
        this.private("target", document.getElementById(args.id));
        this.configure(args);
    };

    Component.prototype.configure = function (args) {
    	/* Override-me to configure your component! */
	};

    Component.prototype.addChild = function () {
        for (var i=0; i<arguments.length; i++) {
            var child = arguments[i];
            if (!child instanceof Component)
                throw MSG_INVALID_ARGUMENT
                	+ ": child must be instance of Component class.";
            this.children.append(child);
            if (child.id)
                this[child.id] = child;
        }
    };

    Component.prototype.setVisible = function (visible) {
    	if (visible) this.show();
    	else this.hide();
    };

    Component.prototype.isVisible = function (){
    	return this.target.style.display != "none";
    };

    Component.prototype.show = function () {
		this.target.style.display = "";
    	this.dispatch ("show");
    };

    Component.prototype.hide = function () {
		this.target.style.display = "none";
		this.dispatch ("hide");
    };

    Component.prototype.setWidth = function (width) {
    	if (!width)return;
    	this.target.style.width = width;
    	this.width = width;
    };

    Component.prototype.setHeight = function (height) {
    	if (!height)return;
    	this.target.style.height = height;
    	this.height = height;
    };

    // FIXME: Make me render myself!
    Component.prototype.render = function () {
    	throw MSG_NOT_IMPLEMENTED_YET + "render method.";
    };

    Component.prototype.event = function (eventName, callback) {
    	if (VALID_WIDGET_DOM_EVENTS.contains(eventName))
    		DomUtil.addEventListener(this.target, eventName, callback);
    	else
    		Component.prototype.event.apply(this, [eventName, callback]);
    };

/* -------------------------------------------------------------------------
 * Widget
 * ------------------------------------------------------------------------- */
    Widget = Class(Component);
    Widget.prototype.toString = function () { return "Generic Widget Object"; };

    Widget.prototype.configure = function (args) {
        this.private("required", args.required);
        delete this.addChild;
    };

    Widget.prototype.getValue = function () {
        return this.target.value;
    };

    Widget.prototype.setValue = function (value) {
        this.target.value = value;
    };

    Widget.prototype.getName = function () {
        return this.target.name;
    };

    Widget.prototype.isRequired = function () {
        return this.required == true;
    };

    Widget.prototype.focus = function () {
        this.target.focus();
    };

/* -------------------------------------------------------------------------
 * BOX
 * ------------------------------------------------------------------------- */
    Box = Class(Component);
    Box.prototype.toString = function () { return "Box Object"; };

/* -------------------------------------------------------------------------
 * Text
 * ------------------------------------------------------------------------- */
    Text = Class(Component);
    Text.prototype.configure = function (args) {
    	this.private ("label", args.label);
    }
    Text.prototype.setLabel = function (label) {
    	this.target.innerHTML = label;
    	this.label = label;
    };

/* -------------------------------------------------------------------------
 * FormItem
 * ------------------------------------------------------------------------- */
    FormItem = Class(Component);
    FormItem.prototype.toString = function () { return "FormItem Object"; };

    FormItem.prototype.configure = function (args) {
    	this.private ("label", args.label);
    	this.private ("labelComponent",
    			document.getElementById(args.id + "Label"));
    };
    FormItem.prototype.setLabel = function (label) {
    	this.labelComponent.innerHTML = label;
    	this.label = label;
    };

/* -------------------------------------------------------------------------
 * Panel
 * ------------------------------------------------------------------------- */
    Panel = Class(Component);
    Panel.prototype.toString = function () { return "Panel Object"; };

    Panel.prototype.configure = function (args){
    	this.private("labelComponent",
    			document.getElementById(args.id+"Label"));
    	this.private("collapsible", args.collapsible);

    	var anchor = document.createElement("a");
    	anchor.setAttribute("name", args.id);
    	this.target.insertBefore(anchor, this.labelComponent.parentNode);

    	if (this.collapsible) {
    		this.private("collapsed");
	    	DomUtil.addEventListener(
	    			this.labelComponent.parentNode, "click",
					this.getMethod("onToogleMode"));
	    	this.setCollapsed(args.collapsed ? true : false);
    	}
    };

    Panel.prototype.setLabel = function (label) {
    	this.labelComponent.innerHTML = label;
    	this.label = label;
    };

    Panel.prototype.onToogleMode = function () {
    	this.setCollapsed(!this.collapsed);
    };

    Panel.prototype.setCollapsed = function (mode) {
    	this.collapsed = mode;
    	var className = mode ? "Collapsed" : "Elapsed";
    	this.target.className = "Panel " + className;
    	
    	var eventName = this.collapsed ? "collapse" : "elapse";
    	this.dispatch (eventName);
    };

    Panel.prototype.addChild = function () {
    	for (var i=0; i<arguments.length; i++) {
            var child = arguments[i];
            if (child instanceof Component)
                child = child.target;
            this.childs.append(child);
            if (child.id)
                this[child.id] = child;
        }
    };

/* -------------------------------------------------------------------------
 * DialogPanel
 * ------------------------------------------------------------------------- */
    DialogPanel = Class(Panel);
    DialogPanel.prototype.toString = function () { return "DialogPanel Object"; };

    DialogPanel.prototype.configure = function (args){
    	Panel.prototype.configure.apply (this, [args]); // super
    	this.private("closable", args.closable);
    	this.private("modal", args.modal);
    	this.private("visible", args.visible);

    	if (args.width)
    		this.setWidth(args.width);

    	if (args.height)
    		this.setHeight(args.height);

    	if (this.closable) {
    		var closeBtn = document.createElement("a");
    		closeBtn.setAttribute("id", args.id+"CloseButton");
    		closeBtn.className = "CloseButton";
    		this.labelComponent.parentNode.appendChild(closeBtn);
    		this.private("closeButton", closeBtn);
	    	DomUtil.addEventListener(closeBtn, "click",
					this.getMethod("onCloseClick"));

	    	if (args.onclose) {
	    		this.event ("close", function(){
	    			eval(args.onclose);
	    		});
	    	}

    	}

    	if (this.visible && this.modal)
    		this.configureAsModal();
    	if (!this.visible && this.modal)
    		this.hide();
    };

    DialogPanel.prototype.configureAsModal = function () {
    	if (!this.width || !this.height)
    		return;

    	this.target.style.display = "";
    	this.target.style.position = "absolute";
    	this.target.style.zIndex = "1000";
    	this.target.style.top = this.getTopPosition() + "px";
    	this.target.style.left = this.getLeftPosition() + "px";

    	this.configureModalBackground();
    };

    DialogPanel.prototype.configureModalBackground = function () {
    	var bg = document.getElementById ( "modalbg" );
    	if (!bg) {
	    	bg = document.createElement("div");
	    	bg.setAttribute("id", "modalbg");
    	}

    	bg.style.position = "absolute";
    	bg.style.width = DomUtil.getWindowWidth() + "px";
    	bg.style.height = DomUtil.getWindowHeight() + "px";
    	bg.style.top = "0px";
    	bg.style.left = "0px";
    	bg.style.zIndex = "500";
    	bg.style.background = "#cdcdcd";

    	DomUtil.applyOpacity (bg, 0);
    	DomUtil.fadeIn(bg, 70);

    	document.getElementsByTagName("body")[0].appendChild(bg);
    };

    DialogPanel.prototype.getLeftPosition = function (args) {
    	var pos = ((DomUtil.getWindowWidth()
    					- this.width.replace("px","")) / 2);
    	return pos;
    };

    DialogPanel.prototype.getTopPosition = function (args) {
    	var pos = ((DomUtil.getWindowHeight()
    					- this.height.replace("px","")) / 2) - 100;
    	return pos;
    };

    DialogPanel.prototype.show = function () {
    	Component.prototype.show.apply(this);
    	if (this.modal)
    		this.configureAsModal();
    };
    
    DialogPanel.prototype.hide = function () {
    	Component.prototype.hide.apply(this);
    	if (this.modal) {
        	var bg = document.getElementById ( "modalbg" );
        	if (bg)
        		bg.parentNode.removeChild(bg);
    	}
    };

    DialogPanel.prototype.onCloseClick = function (){
    	this.hide();
    	this.dispatch ("close");
    };

/* -------------------------------------------------------------------------
 * TextInput
 * ------------------------------------------------------------------------- */
    TextInput = Class(Widget);
    TextInput.prototype.toString = function () { return "TextInput Object"; };

    TextInput.prototype.configure = function (args) {
    	this.private("mask", args.mask);
    	DomUtil.addEventListener(this.target, "keyup",
				this.getMethod("onKeyUp"));
    };

    TextInput.prototype.onKeyUp = function (){
    	if (!this.mask)
    		return;

        var str = this.target.value;
        this.target.value = TextMask.applyMask(str, this.mask);
    };

/* -------------------------------------------------------------------------
 * Checkbox
 * ------------------------------------------------------------------------- */
    Checkbox = Class(Widget);
    Checkbox.prototype.toString = function () { return "Checkbox Object"; };

    Checkbox.prototype.isChecked = function () {
        return (this.target.checked == "checked"
        	 || this.target.checked == true);
    };

    Checkbox.prototype.setChecked = function (value) {
        this.target.checked = (value == "checked" || value == true)
        							? "checked" : "";
    };

/* -------------------------------------------------------------------------
 *  Textarea
 * ------------------------------------------------------------------------- */
    Textarea = Class(TextInput);
    Textarea.prototype.toString = function () { return "Textarea Object"; };

/* -------------------------------------------------------------------------
 *  Button
 * ------------------------------------------------------------------------- */
    Button = Class(Component);
    Button.prototype.toString = function () { return "Button Object"; };

    Button.prototype.setLabel = function (value) {
    	this.target.value = value;
    	this.label = value;
    };

    Button.prototype.getLabel = function (){
    	return this.target.value;
    };

/* -------------------------------------------------------------------------
 *  ComboBox
 * ------------------------------------------------------------------------- */
    ComboBox = Class(Widget);
    ComboBox.prototype.toString = function () { return "ComboBox Object"; };

/* -------------------------------------------------------------------------
 *  Radiobox
 * ------------------------------------------------------------------------- */
    Radiobox = Class(Checkbox);
    Radiobox.prototype.toString = function () { return "Radiobox Object"; };

/* -------------------------------------------------------------------------
 *  RadioGroup 
 * ------------------------------------------------------------------------- */
    RadioGroup = Class(Widget);
    RadioGroup.prototype.toString = function () { return "RadioGroup Object"; };

    RadioGroup.prototype.configure = function (args) {
    	Widget.prototype.configure.apply (this, [args]);
    	var targetIds = args.target;
        for (var i=0; i<targetIds.length; i++) {
            var radioId = targetIds[i];
            if (!radioId) continue;
            var radio = new Radiobox({id: radioId});
            this.addChild(radio);
            this.target = radio.target;
        }
    };

    RadioGroup.prototype.setValue = function (value) {
        for (var i=0; i<this.childs.length; i++) {
            var child = this.childs[i];
            var isChecked = child.getValue() == value;
            child.setChecked(isChecked);
        }
    };

    RadioGroup.prototype.getValue = function (value) {
        for (var i=0; i<this.childs.length; i++) {
            var child = this.childs[i];
            if (child.isChecked()) {
                return child.getValue();
            }
        }
        return null;
    };

/* -------------------------------------------------------------------------
 *  Grid
 * ------------------------------------------------------------------------- */
    Grid = Class(Widget);
    Grid.prototype.toString = function () { return "Grid Object"; };
    Grid.prototype.configure = function (args) {
    	Widget.prototype.configure.apply (this, [args]);
        this.private("value");
        this.private("columns");
        this.private("rows");
        this.private("selectedItems", null);

        this.configureGrid();

        this.setColumns( args.columns || new Array() );
        this.setValue( args.value || new Array() );

        this.setWidth ( args.width );
        this.setHeight ( args.height );
        this.flush();
    };

    Grid.prototype.configureGrid = function (){
    	var id = this.id;
    	var grid = document.getElementById(id);

    	var gridHeader = document.createElement("ul");
    	gridHeader.className = "GridHeader";
    	gridHeader.setAttribute("id", id+"GridHeader");

    	var gridBody = document.createElement("div");
    	gridBody.className = "GridBody";
    	gridBody.setAttribute("id", id+"GridBody");

    	var gridHolder = document.createElement("input");
    	gridHolder.setAttribute("type","hidden");
    	gridHolder.setAttribute("id",id+"GridValueHolder");
    	gridHolder.setAttribute("name", id);

    	grid.appendChild(gridHolder);
    	grid.appendChild(gridHeader);
    	grid.appendChild(gridBody);

    	this.private("target", grid);
    	this.private("gridHeader", gridHeader);
    	this.private("gridBody", gridBody);
    	this.private("gridHolder", gridHolder);

    };

    Grid.prototype.setValue = function (data) {
    	if (!data) return;
    	this.clean();

    	for (var i=0; i<data.length; i++)
			if (data[i])
				this.addRow (data[i], i);

    	this.value = data;
    };

    Grid.prototype.clean = function () {
    	var body = this.gridBody;
    	for (var i=body.childNodes.lenght-1; i>=0; i--)
    		body.removeChild(body.childNodes[i]);
    	this.rows = new Array();
    };

    Grid.prototype.setColumns = function (columns) {
    	this.columns = columns;
    	for (var i=0; i<columns.length; i++) {
    		var columnData = columns[i];
    		if (!columnData) continue;
    		var col = this.addColumn(columnData);
    		this.columns.append(col);
    	}
    };

    Grid.prototype.addColumn = function (columnData) {
    	var header = this.gridHeader;
    	var ColumnClass = GridColumnFactory
				.registeredColumnTypes[
				     unescape(columnData.type || "defaultColumn")];
    	var column = new ColumnClass(columnData, this).render();
		header.appendChild(column);
    };

    Grid.prototype.addRow = function (rowData) {
    	var rowPos = this.rows.length;
    	var renderedRow = this.registerRow (rowPos, rowData);
    	this.gridBody.appendChild (renderedRow);
    };

    Grid.prototype.updateRow = function (index, rowData) {
    	var row = this.getRow(index).target;
    	var newRow = this.registerRow (index, rowData);
    	row.parentNode.insertBefore(newRow, row);
    	this.gridBody.removeChild ( row );
    };

    Grid.prototype.removeRow = function (index) {
    	var row = this.getRow(index);
    	this.gridBody.removeChild(row.target);

    	var rows = new Array();
    	for (var i=0; i<this.rows.length; i++)
    		if (this.rows[i] != row)
    			rows.append(this.rows[i]);

    	this.rows = rows;
    	this.flush();
    	return row;
    }

    Grid.prototype.registerRow = function (index, rowData) {
    	var row = new GridRow(rowData, index, this);
    	this.rows[index] = row;
    	var renderedRow = row.render();
    	DomUtil.addEventListener(renderedRow, "click",
				this.getMethod("onRowClick", row));
    	this.flush();
    	return renderedRow;
    };

    Grid.prototype.getRow = function (index) {
    	return this.rows[index];
    };

    Grid.prototype.onRowClick = function (event, row) {
    	this.selectRow(row);
    	this.dispatch ("rowclick", row);
    };

    // FIXME: Make me select more than one row at once
    Grid.prototype.selectRow = function (gridRow) {
    	for (var i=0; i<this.rows.length; i++) {
    		var row = this.rows[i];
    		if (!row) continue;
    		row.target.className = 
    			row.target.className.replace("selected", "");
    	}
    	gridRow.target.className+= " selected";
    	this.selectedItems = [gridRow];
    };

    Grid.prototype.flush = function () {
    	var buffer = "";
    	var values = new Array();
    	for (var index in this.rows) {
    		var item = this.rows[index].value;
    		values.append(item);
    		if (!item) continue;

    		for (var attr in item)
    			buffer+= attr + "=" + escape(item[attr]) + "&";
    		buffer+= "?";
    	}
    	this.gridHolder.value = buffer;
    	this.value = values;
    };

    Grid.prototype.setHeight = function (height) {
		if (!height) return;
		this.gridBody.style.height = height;
	};

/* -------------------------------------------------------------------------
 *  GridColumn
 * ------------------------------------------------------------------------- */
    GridRow = Class();
    GridRow.prototype.toString = function () { return "GridRow Object"; };
    GridRow.prototype.constructor = function (values, rowPos, parent) {
    	this.private ("parent", parent);
    	this.private ("id", parent.id + "GridRow" + rowPos);
    	this.private ("value", values);
    	this.private ("highlighted", rowPos % 2 == 0);
    	this.private ("selected", false);
    	this.private ("index", rowPos);
    };

    GridRow.prototype.render = function () {
    	var target = document.createElement("ul");
    	target.setAttribute ("id", this.id);
    	
    	var className = "";
    	if (this.highlighted)
    		className = "highlighted";
    	if (this.selected)
    		className+= " selected";
    	target.className = className;

    	for (var i=0; i<this.parent.columns.length; i++) {
    		var headerColumn = this.parent.columns[i];
    		if (!headerColumn) continue;
    		
    		var ColumnClass = GridColumnFactory
    				.registeredColumnTypes[
    				     unescape(headerColumn.type || "defaultColumn")];
    		
    		var columnData = {
    			id: headerColumn.id,
    			value: this.value[headerColumn.id] || headerColumn.value,
    			width: headerColumn.width,
    			header: headerColumn
    		};
    		
    		var renderedColumn = new ColumnClass(columnData, this).render();
    		target.appendChild ( renderedColumn );
    	}
    	this.target = target;
    	return target;
    };
    
    GridRow.prototype.flush = function () { this.parent.flush(); };

/* -------------------------------------------------------------------------
 *  GridColumn
 * ------------------------------------------------------------------------- */
    GridColumn = Class();
    GridColumn.prototype.constructor = function (args, parent) {
    	this.private ("parent", parent);
    	this.private ("id", unescape(args.id));
    	this.private ("value", unescape(args.value || args.label));
    	this.private ("width", unescape(args.width));
    	this.configure(args);
    };

    GridColumn.prototype.configure = function (args) {};

    GridColumn.prototype.render = function () {
    	var column = document.createElement("li");
		column.appendChild(this.renderContent());
		column.setAttribute("id", this.parent.id + "Column" + this.id);

		if (this.width)
			column.setAttribute("style", "width:" + this.width);
		return column;
    };

    GridColumn.prototype.flush = function () {
    	this.parent.value[this.id] = this.getValue();
    	this.parent.flush();
    };

    GridColumn.prototype.renderContent = function () {
    	return document.createTextNode(this.value);
    };

/* -------------------------------------------------------------------------
 *  GridImageColumn
 * ------------------------------------------------------------------------- */
    GridImageColumn = Class(GridColumn);
    GridImageColumn.prototype.configure = function (args) {
    	if (args.header) {
    		var onclick = unescape(args.header.click);
    		this.event ("iconclick", function (data, index) {
    			eval(onclick);
    		});
    	}
    };

    GridImageColumn.prototype.renderContent = function () {
		var img = document.createElement("img");
		img.setAttribute("src", this.value);

		DomUtil.addEventListener(img, "click",
				this.getMethod("onImageClick"));

		return img;
    };

    GridImageColumn.prototype.onImageClick = function (event){
    	var data = this.parent.value,
			index = this.parent.index;
    	this.dispatch("iconclick", data, index);
    };

/* -------------------------------------------------------------------------
 *  GridColumnFactory
 * ------------------------------------------------------------------------- */
    GridColumnFactory = {
    	registeredColumnTypes: {
    		defaultColumn: GridColumn,
    		image: GridImageColumn
		},
    	register: function (columnType, clazz){
    		GridColumnFactory.registeredColumnTypes[columnType] = clazz;
    	}
    };

/* -------------------------------------------------------------------------
 *  Tree
 * ------------------------------------------------------------------------- */
    Tree = Class(Widget);
    Tree.prototype.configure = function (args) {
    	this.private ("children", new Object());
    	this.private ("service", args.service);
    	this.private ("selectedChildren", args.selectedChildren || new Array());
    	this.private ("root", new TreeItem({
    		id: "root",
    		isRootNode: true
    	}));

    	this.render(args);
    };

    Tree.prototype.setSelectedChildren = function (children) {
    	this.selectedChildren = children;
    	
    	if (children)
	    	for (var childId in this.getChildren()) {
	    		var child = this.getChild(childId);
	    		child.setSelected(this.selectedChildren.contains(childId));
	    	}
    };

    Tree.prototype.render = function (args) {
    	var input = document.createElement("input");
    	input.setAttribute("type","hidden");
    	input.setAttribute("name", this.id);
    	this.hiddenField = input;

    	this.root.render();
    	this.target.appendChild(input);
    	this.target.appendChild(this.root.target);

    	if (args.children)
    		this.setChildren(args.children);
    };

    Tree.prototype.setChildren = function ( children ) {
    	this.children = new Object();
    	for (var i=0; i<children.size(); i++) {
    		var child = children[i];
    		if (child)
    			this.appendChild(child);
    	}
    	this.flush();
    };

    Tree.prototype.appendChild = function (item) {
    	item.tree = this;

    	var rootNode = this.root;
    	if (this.children[item.parentId])
    		rootNode = this.children[item.parentId];

    	var child = rootNode.appendChild ( item );
    	child.event ("elapse", this.getMethod("requestChildrenService"));
    	child.event ("select", this.getMethod("onSelectChild"));
    	child.event ("unselect", this.getMethod("onUnselectChild"));
    	child.setSelected(this.selectedChildren.contains(child.id));
    	this.children[item.id] = child;
    };

    Tree.prototype.getChild = function (id) {
    	return this.children[id];
    };

    Tree.prototype.onSelectChild = function (child) {
    	this.selectedChildren.append(child.id);
    	this.flush();
    	this.dispatch ("selectchild", child);
    };

    Tree.prototype.onUnselectChild = function (child) {
    	this.selectedChildren.remove(child.id);
    	this.flush();
    	this.dispatch ("unselectchild", child);
    };

    Tree.prototype.flush = function () {
    	var buffer = "";
    	for (var i=0; i<this.selectedChildren.size(); i++) {
    		buffer+= escape(this.selectedChildren[i]) + ";";
    	}
    	this.hiddenField.value = buffer;
    };

    Tree.prototype.requestChildrenService = function (child) {
    	if (!this.service
		||  !child.hasChildren
		||   child.children.size() > 0)
    		return;

    	var treeitem = this;
    	var handleResponse = function (response) {
    		var children = JSON.parse(response);
    		for (var i=0; i<children.size(); i++) {
    			var child = children[i];
    			treeitem.appendChild(child);
    		}
    	}

    	var http = new HttpRequest(this.service + "?id=" + child.id);
    	http.event("ready", handleResponse);
    	http.send();
    };

/* -------------------------------------------------------------------------
 *  TreeItem
 * ------------------------------------------------------------------------- */
    TreeItem = Class();
    TreeItem.prototype.constructor = function (args) {
        if (!args)
            throw MSG_INVALID_ARGUMENT_LIST;

    	this.private ("id", args.id);
    	this.private ("parentId", args.parentId);
    	this.private ("tree", args.tree);
    	this.private ("label", args.label);
    	this.private ("hasChildren", (
			args.hasChildren == "true" || args.hasChildren == true));
    	this.private ("children", new Array());
    	this.private ("collapsed", (args.collapsed == false) ? false : true);
    	this.private ("isRootNode", args.isRootNode || false);
    };

    TreeItem.prototype.render = function () {
    	var ul = document.createElement("ul");
    	this.private("target", ul);

    	if (this.isRootNode)
    		return null;

    	var a = document.createElement("a");
    	a.setAttribute("href","javascript:void(0);");
    	this.anchorLink = a;
    	DomUtil.addEventListener(this.anchorLink, "click",
    			this.getMethod("onToogleElapseCollapse", this));

    	var input = document.createElement("input");
    	input.setAttribute("type", "checkbox");
    	input.setAttribute("id", this.tree.id + "Checkbox" + this.id);
    	this.checkbox = input;
    	// FIXME: parei aqui! :D
    	DomUtil.addEventListener(this.checkbox, "click",
    			this.getMethod("onCheckboxClick", this));

    	var labelText = document.createTextNode(this.label);
    	this.labelText = input;

    	var label = document.createElement("label");
    	label.appendChild(input);
    	label.appendChild(labelText);

    	var li = document.createElement("li");
    	li.appendChild(a);
    	li.appendChild(label);
    	li.appendChild(ul);
    	this.private("container", li);

    	this.flush();
    	return li;
    };

    TreeItem.prototype.onCheckboxClick = function () {
    	var eventName = this.isSelected() ? "select" : "unselect";
    	this.dispatch (eventName, this);
    };

    TreeItem.prototype.setLabel = function ( label ) {
    	this.labelText.innerHTML = label;
    	this.label = label;
    };

    TreeItem.prototype.isSelected = function () {
        return (this.checkbox.checked == "checked"
        	 || this.checkbox.checked == true);
    };

    TreeItem.prototype.setSelected = function (value) {
    	this.checkbox.checked = (value == "checked" || value == true)
    								? "checked" : "";
    };

    TreeItem.prototype.setChildren = function ( children ) {
    	if (children)
	    	for (var i=0; i<children.size(); i++)
	    		if (children[i])
	    			this.appendChild(children[i]);
    	this.children = children;
    	this.hasChildren = (children && children.size() > 0);
    };

    TreeItem.prototype.flush = function (  ) {
    	if (this.isRootNode)
    		return;
    	
    	if (!this.hasChildren
    	&&   this.children.size() == 0)
    	{
    		this.anchorLink.className = "";
    		return;
    	}

    	if (this.collapsed) {
    		this.anchorLink.className = "collapsed";
    		this.target.style.display = "none";
    	} else {
        	this.anchorLink.className = "elapsed";
    		this.target.style.display = "";
    	}
    };

    TreeItem.prototype.appendChild = function (itemData) {
    	var item = new TreeItem ( itemData );
    	var renderedItem = item.render ( );
    	this.target.appendChild ( renderedItem );

    	if (itemData.children)
    		this.setChildren ( itemData.children );

    	this.children.append(item);
    	this.hasChildren = true;
    	this.flush();

    	return item;
    };

    TreeItem.prototype.onToogleElapseCollapse = function (e, item) {
    	if (!this.hasChildren)
       		return;

    	this.collapsed = !this.collapsed;

    	var eventName = this.collapsed ? "collapse" : "elapse";
    	this.dispatch (eventName, this);

    	this.flush();
    };
