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
            'd':/\d/,
            'M':/\d/,
            'm':/\d/,
            'h':/\d/,
            'y':/\d/,
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

    Component.prototype.setMinWidth = function (width) {
    	if (!width)return;
    	this.target.style.minWidth = width;
    	this.minWidth = width;
    };

    Component.prototype.setMinHeight = function (height) {
    	if (!height)return;
    	this.target.style.minHeight = height;
    	this.minHeight = height;
    };

    // FIXME: Make me render myself!
    Component.prototype.render = function () {
    	throw MSG_NOT_IMPLEMENTED_YET + "render method.";
    };

    // TODO: Check this event handler.
    Component.prototype.event = function (eventName, callback) {
    	var self = this;
    	if (VALID_WIDGET_DOM_EVENTS.contains(eventName)
    			&& !this.events[eventName])
    		DomUtil.addEventListener(this.target, eventName, function (){
    			self.dispatch(eventName);
    		});
    	Class.prototype.event.apply(this, [eventName, callback]);
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
    Box.prototype.toString = function () { return "Box"; };

/* -------------------------------------------------------------------------
 * Image
 * ------------------------------------------------------------------------- */
    Image = Class(Component);
    Image.prototype.toString = function () { return "Image"; };

    Image.prototype.configure = function (args) {
    	this.private("location", args.location);
    };

    Image.prototype.setLocation = function (location) {
    	this.target.setAttribute("href", location);
    	this.location=location;
    };

/* -------------------------------------------------------------------------
 * Text
 * ------------------------------------------------------------------------- */
    Text = Class(Component);
    Text.prototype.configure = function (args) {
    	this.private ("text", args.text);
    };
    Text.prototype.setText = function (text) {
    	this.target.innerHTML = text;
    	this.text = text;
    };

/* -------------------------------------------------------------------------
 * FormItem
 * ------------------------------------------------------------------------- */
    FormItem = Class(Component);
    FormItem.prototype.toString = function () { return "FormItem"; };

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
    Panel.prototype.toString = function () { return "Panel"; };

    Panel.prototype.configure = function (args){
    	this.private("labelComponent",
    			document.getElementById(args.id+"Label"));

    	var anchor = document.createElement("a");
    	anchor.setAttribute("name", args.id);
    	this.target.insertBefore(anchor, this.labelComponent.parentNode);
    };

    /*Panel.prototype.setHeight = function (height) {
    	Panel.prototype.setHeight.apply (this, [height]); // super
    	if (height)
    		this.target.style.overflow = "auto";
    };*/

    Panel.prototype.setLabel = function (label) {
    	this.labelComponent.innerHTML = label;
    	this.label = label;
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
 * Dialog
 * ------------------------------------------------------------------------- */
    Dialog = Class(Panel);
    Dialog.prototype.toString = function () { return "Dialog"; };

    Dialog.prototype.configure = function (args){
    	Panel.prototype.configure.apply (this, [args]); // super
    	this.private("closable", args.closable);
    	this.private("modal", args.modal);
    	this.private("visible", args.visible);

    	if (args.width)
    		this.setWidth(args.width);

    	if (args.height || args.height != "null")
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

    Dialog.prototype.configureAsModal = function () {
    	if (!this.width || !this.height)
    		return;

    	this.target.style.display = "";
    	this.target.style.position = "absolute";
    	this.target.style.zIndex = "1000";
    	this.target.style.top = this.getTopPosition() + "px";
    	this.target.style.left = this.getLeftPosition() + "px";

    	this.configureModalBackground();
    };

    Dialog.prototype.configureModalBackground = function () {
    	var bg = document.getElementById ( "modalbg" );
    	if (!bg) {
	    	bg = document.createElement("div");
	    	bg.setAttribute("id", "modalbg");
    	}
    	
    	var windowSize = DomUtil.getWindowDimensions();
    	var scrollSize = DomUtil.getWindowScroll();
    	var windowWidth = windowSize.width + scrollSize.x,
    		windowHeight = windowSize.height + scrollSize.y;

    	bg.style.position = "absolute";
    	bg.style.width = windowWidth + "px";
    	bg.style.height = windowHeight + "px";
    	bg.style.top = "0px";
    	bg.style.left = "0px";
    	bg.style.zIndex = "500";
    	bg.style.background = "#cdcdcd";
    	bg.style.display = "none";
    	DomUtil.applyOpacity (bg, 0);

    	document.getElementsByTagName("body")[0].appendChild(bg);

    	DomUtil.applyOpacity (bg, 0);
    	bg.style.display = "";
    	DomUtil.fade(bg, 0, 70);
    };

    Dialog.prototype.getLeftPosition = function (args) {
    	var pos = ((DomUtil.getWindowDimensions().width
    					- this.width.replace("px","")) / 2);
    	return pos;
    };

    Dialog.prototype.getTopPosition = function (args) {
    	var pos = (DomUtil.getWindowScroll().y) + 100;
    	return pos;
    };

    Dialog.prototype.show = function () {
    	Component.prototype.show.apply(this);
    	if (this.modal)
    		this.configureAsModal();
    };
    
    Dialog.prototype.hide = function () {
    	Component.prototype.hide.apply(this);
    	if (this.modal) {
        	var bg = document.getElementById ( "modalbg" );
        	if (bg)
        		bg.parentNode.removeChild(bg);
    	}
    };

    Dialog.prototype.onCloseClick = function (){
    	this.hide();
    	this.dispatch ("close");
    };

/* -------------------------------------------------------------------------
 * TextInput
 * ------------------------------------------------------------------------- */
    TextInput = Class(Widget);
    TextInput.prototype.toString = function () { return "TextInput"; };

    TextInput.prototype.configure = function (args) {
    	Widget.prototype.configure.apply (this, [args]);
    	this.private("mask", args.mask);
    	this.private("readonly", args.readonly);
    	DomUtil.addEventListener(this.target, "keyup",
				this.getMethod("onKeyUp"));
    };

    TextInput.prototype.onKeyUp = function (){
    	if (!this.mask)
    		return;

        var str = this.target.value;
        this.target.value = TextMask.applyMask(str, this.mask);
    };
    
    TextInput.prototype.setReadonly = function (value) {
    	var isReadonly = (value && value != "false");
    	this.target.setAttribute("readonly",
    			isReadonly ? "readonly": "");
    	this.readonly = isReadonly;
    };

/* -------------------------------------------------------------------------
 * Checkbox
 * ------------------------------------------------------------------------- */
    Checkbox = Class(Widget);
    Checkbox.prototype.toString = function () { return "Checkbox"; };
    
    Checkbox.prototype.configure = function (args) {
    	this.labelComponent = document.getElementById(this.id);
    };

    Panel.prototype.setLabel = function (label) {
    	this.labelComponent.innerHTML = label;
    	this.label = label;
    };

    Checkbox.prototype.isChecked = function () {
        return (this.target.checked == "checked"
        	 || this.target.checked == true);
    };

    Checkbox.prototype.setChecked = function (value) {
        this.target.checked = (value == "checked" || value == true)
        							? "checked" : "";
    };

    Checkbox.prototype.getValue = function (value) {
    	return this.isChecked() ? "on" : "";
    };

/* -------------------------------------------------------------------------
 *  TextArea
 * ------------------------------------------------------------------------- */
    TextArea = Class(TextInput);
    TextArea.prototype.toString = function () { return "Textarea"; };

/* -------------------------------------------------------------------------
 *  Button
 * ------------------------------------------------------------------------- */
    Button = Class(Component);
    Button.prototype.toString = function () { return "Button"; };

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
    ComboBox.prototype.toString = function () { return "ComboBox"; };

/* -------------------------------------------------------------------------
 *  Radiobox
 * ------------------------------------------------------------------------- */
    Radiobox = Class(Checkbox);
    Radiobox.prototype.toString = function () { return "Radiobox"; };

/* -------------------------------------------------------------------------
 *  RadioGroup 
 * ------------------------------------------------------------------------- */
    RadioGroup = Class(Widget);
    RadioGroup.prototype.toString = function () { return "RadioGroup"; };

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
        for (var i=0; i<this.children.length; i++) {
            var child = this.children[i];
            var isChecked = child.getValue() == value;
            child.setChecked(isChecked);
        }
    };

    RadioGroup.prototype.getValue = function (value) {
        for (var i=0; i<this.children.length; i++) {
            var child = this.children[i];
            if (child.isChecked()) {
                return child.getValue();
            }
        }
        return null;
    };

/* -------------------------------------------------------------------------
 *  Grid
 * ------------------------------------------------------------------------- */
    var EVENT_GRIDCOLUMN_VISIBILITYCHANGE = "visibilityChange";
    var EVENT_GRID_REQUESTFLUSH = "requestFlush";
    var EVENT_GRID_SELECTROW = "selectrow";

    Grid = Class(Widget);
    Grid.prototype.toString = function () { return "Grid"; };
    Grid.prototype.configure = function (args) {
    	Widget.prototype.configure.apply (this, [args]);
        this.private("value");
        this.private("columns");
        this.private("rows");
        this.private("selectedItems", new Array());
        this.private("multipleSelection", args.multipleSelection);

        this.configureGrid();

        this.setColumns( args.columns || new Array() );
        this.setValue( args.value || new Array() );

        this.setWidth ( args.width );
        this.setMinWidth ( args.width );
        this.setHeight ( args.height );
        this.setMinHeight ( args.height );
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
    	for (var i=body.childNodes.length-1; i>=0; i--)
    		body.removeChild(body.childNodes[i]);
    	this.rows = new Array();
    };

    Grid.prototype.setColumns = function (columns) {
    	this.columns = new Array();//columns;
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

    	var column = new ColumnClass(columnData, this);
    	column.event(
    			EVENT_GRID_REQUESTFLUSH,
    			this.getMethod("flush")
			);
		header.appendChild(column.render());

		return column;
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

    Grid.prototype.removeRow = function (row) {
    	if (this.selectedItems.contains(row))
    		this.selectedItems.remove(row);

    	this.gridBody.removeChild(row.target);

    	var rows = new Array();
    	for (var i=0; i<this.rows.length; i++)
    		if (this.rows[i] != row)
    			rows.append(this.rows[i]);

    	this.rows = rows;
    	this.flush();
    	return row;
    };

    Grid.prototype.removeRowAt = function (index) {
    	var row = this.getRow(index);
    	return this.removeRow(row);
    };

    Grid.prototype.registerRow = function (index, rowData) {
    	var row = new GridRow(rowData, index, this);
    	row.event(
			EVENT_GRID_REQUESTFLUSH,
			this.getMethod("flush"));
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

    Grid.prototype.onRowClick = function (row) {
    	this.trySelectRow(row);
    	this.dispatch (EVENT_GRID_SELECTROW, row);
    };

    Grid.prototype.trySelectRow = function (gridRow) {
    	if (!this.multipleSelection) {
	    	this.unselectRows();
	    	this.selectRow(gridRow);
    	} else if (gridRow.getSelected() == true)
    		this.unselectRow(gridRow);
    	else
    		this.selectRow(gridRow);
    };

    Grid.prototype.selectRow = function (gridRow) {
    	gridRow.setSelected(true);
    	this.selectedItems.append(gridRow);
    };

    Grid.prototype.unselectRow = function (gridRow) {
    	gridRow.setSelected(false);
    	this.selectedItems.remove(gridRow);
    };

    Grid.prototype.unselectRows = function (){
    	for (var i=0; i<this.rows.length; i++) {
    		var row = this.rows[i];
    		if (!row) continue;
    		this.unselectRow(row);
    	}
    };

    Grid.prototype.flush = function () {
    	var buffer = "";
    	var values = new Array();
    	for (var index=0; index<this.rows.length; index++) {
    		this.rows[index].flush();
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
 *  GridUtils (static)
 * ------------------------------------------------------------------------- */
	GridUtils = {
		getData: function (grid) {
			var rows = grid.getRows();
			var data = new Array();

			for (var i=0; i<rows.length; i++)
				data.append ( rows[i].getValue() );

			return data;
		},
		getSelectedItemsData: function (grid) {
			var rows = grid.getSelectedItems();
			var data = new Array();

			for (var i=0; i<rows.length; i++)
				data.append ( rows[i].getValue() );

			return data;
		},
		serializeData: function (grid) {
			var data = GridUtils.getData(grid);
			var buffer = "";
			
			for (var i=0; i<data.length; i++) {
				for (var attr in data[i])
	    			buffer+= attr + "=" + escape(data[i][attr] || "") + "&";
	    		buffer+= "?";
			}
			return buffer;
		},
		serializeSelectedItemsData: function (grid) {
			var data = GridUtils.getSelectedItemsData(grid);
			var buffer = "";
			
			for (var i=0; i<data.length; i++) {
				for (var attr in data[i])
	    			buffer+= attr + "=" + escape(data[i][attr] || "") + "&";
	    		buffer+= "?";
			}
			return buffer;
		}
	};

/* -------------------------------------------------------------------------
 *  GridColumn
 * ------------------------------------------------------------------------- */
    GridRow = Class();
    GridRow.prototype.toString = function () { return "GridRow"; };
    GridRow.prototype.constructor = function (values, rowPos, parent) {
    	this.private ("parent", parent);
    	this.private ("id", parent.id + "GridRow" + rowPos);
    	this.private ("value", values);
    	this.private ("highlighted", rowPos % 2 == 0);
    	this.private ("selected", false);
    	this.private ("index", rowPos);
    	this.private ("columns", new Array())
    };

    GridRow.prototype.setSelected = function (selected){
    	if (selected)
    		this.target.className+= " selected";
    	else
    		this.target.className = 
    			this.target.className
    				.replace("selected", "")
    				.replace("  "," ");

    	this.selected = selected;
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

    		var self = this;
    		var column = new ColumnClass(columnData, this);
    		column.event (
    				EVENT_GRID_REQUESTFLUSH,
    				this.getMethod("onColumnRequestFlush"));
    		this.columns.append(column);

    		var renderedColumn = column.render();
    		target.appendChild ( renderedColumn );
    	}
    	this.target = target;
    	return target;
    };

    GridRow.prototype.onColumnRequestFlush = function (){
    	this.dispatch(EVENT_GRID_REQUESTFLUSH);
    };

    GridRow.prototype.flush = function () {
    	for (var i=0; i<this.columns.length; i++)
    		this.columns[i].flush();
	};

/* -------------------------------------------------------------------------
 *  GridColumn
 * ------------------------------------------------------------------------- */
    GridColumn = Class();
    GridColumn.prototype.constructor = function (args, parent) {
    	this.private ("parent", parent);
    	this.private ("id", unescape(args.id));
    	this.private ("value", unescape(args.value || args.label));
    	this.private ("width", unescape(args.width));
    	this.private ("visible", (args.visible != false && args.visible != "false"));
    	this.private ("header", args.header);
    	this.private ("type", args.type);

    	this.configure(args);
    };

    GridColumn.prototype.configure = function (args) {};

    GridColumn.prototype.render = function () {
    	var column = document.createElement("li");
		column.appendChild(this.renderContent());
		column.setAttribute("id", this.parent.id + "Column" + this.id);
		this.target = column;

		var style = "";
		if (this.width)
			style+= "width:" + this.width + ";";
		if (!this.visible)
			style+= "display: none;";

		column.setAttribute("style", style);
		return column;
    };

    GridColumn.prototype.setVisible = function (value) {
    	this.visible = value;
    	this.target.style.display = (this.visible) ? "" : "none";
    	if (!this.header)
    		this.dispatch(EVENT_GRID_REQUESTFLUSH);
    };

    GridColumn.prototype.isVisible = function (){
    	return this.visible;
    };

    GridColumn.prototype.flush = function () {
    	this.parent.value[this.id] = this.getValue();
    	if (this.header)
    		this.setVisible(this.header.isVisible());
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
    		this.event ("iconclick", function (rowdata, rowindex) {
    			eval(onclick);
    		});
    	} else
    		this.private("click", args.click);
    };

    GridImageColumn.prototype.renderContent = function () {
		var img = document.createElement("img");
		img.setAttribute("src", this.value);

		DomUtil.addEventListener(img, "click",
				this.getMethod("onImageClick"));

		return img;
    };

    GridImageColumn.prototype.onImageClick = function (event){
    	var rowdata = this.parent.value,
    		rowindex = this.parent.index;
    	this.dispatch("iconclick", rowdata, rowindex);
    };

/* -------------------------------------------------------------------------
 *  GridColumnFactory
 * ------------------------------------------------------------------------- */
    GridColumnFactory = {
    	registeredColumnTypes: {
    		defaultColumn: GridColumn,
    		date: GridColumn,
    		time: GridColumn,
    		timestamp: GridColumn,
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
    
    Tree.prototype.clean = function () {
    	this.root.clean();
    	this.children = new Object();
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

    TreeItem.prototype.clean = function () {
    	var body = this.target;

    	for (var i=body.childNodes.length-1; i>=0; i--)
    		body.removeChild(body.childNodes[i]);

    	this.children = new Array();
    	this.collapsed = true;
    	this.flush();
    };
