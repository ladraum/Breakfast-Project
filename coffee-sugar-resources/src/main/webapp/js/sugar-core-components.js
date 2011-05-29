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

var MSG_INVALID_ARGUMENT_LIST = "Invalid arguments list. Components excepts at least \"{id: ''}\" as argument.";
var MSG_INVALID_ID_ARGUMENT   = "Invalid 'id' argument: Unknown object.";
var MSG_INVALID_ARGUMENT      = "Invalid argument: ";

/* -------------------------------------------------------------------------
 * Component
 * ------------------------------------------------------------------------- */
    Component = Class();

    Component.prototype.constructor = function (args) {
        if (!args)
            throw MSG_INVALID_ARGUMENT_LIST;
        this.private("label", args.label || "");
        this.private("childs", new Array());
        this.private("id",     args.id);
    };

    Component.prototype.addChild = function () {
        for (var i=0; i<arguments.length; i++) {
            var child = arguments[i];
            if (!child instanceof Component)
                throw MSG_INVALID_ARGUMENT + ": childs must be instance of Component class.";
            this.childs.append(child);
            if (child.id)
                this[child.id] = child;
        }
    };

/* -------------------------------------------------------------------------
 * Widget
 * ------------------------------------------------------------------------- */
    Widget = Class(Component);

    Widget.prototype.constructor = function (args) {
        Component.prototype.constructor.apply(this, [args]); // super

        if (!args.target && args.id)
            args.target = document.getElementById(args.id);

        if (!args.target)
            throw MSG_INVALID_ID_ARGUMENT;

        this.private("target", args.target);
        this.private("label",  args.label || args.id);
        this.private("required", args.required);
    };

    Widget.prototype.get = function (attribute) {
        return this.target[attribute];
    };

    Widget.prototype.set = function (attribute, value, isAttribute) {
        if (isAttribute)
            this.setAttribute(attribute, value);
        else
            this.target[attribute] = value;
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

    Widget.prototype.event = function (eventName, callback) {
        EventHandler.addEventListener(this.target, eventName, callback);
    };

    Widget.prototype.focus = function () {
        this.target.focus();
    };

/* -------------------------------------------------------------------------
 * BOX Implementation
 * ------------------------------------------------------------------------- */
    Box = Class(Component);

/* -------------------------------------------------------------------------
 * TextInput Implementation
 * ------------------------------------------------------------------------- */
    TextInput = Class(Widget);

/* -------------------------------------------------------------------------
 * Checkbox Implementation
 * ------------------------------------------------------------------------- */
    Checkbox = Class(Widget);

    Checkbox.prototype.isChecked = function () {
        return (this.target.checked == "checked" || this.target.checked == true);
    };

    Checkbox.prototype.setChecked = function (value) {
        this.target.checked = (value == "checked" || value == true) ? "checked" : "";
    };

/* -------------------------------------------------------------------------
 *  Textarea Implementation
 * ------------------------------------------------------------------------- */
    Textarea = Class(TextInput);
/* -------------------------------------------------------------------------
 *  ComboBox Implementation
 * ------------------------------------------------------------------------- */
    ComboBox = Class(Widget);

/* -------------------------------------------------------------------------
 *  Radiobox Implementation
 * ------------------------------------------------------------------------- */
    Radiobox = Class(Checkbox);

/* -------------------------------------------------------------------------
 *  Radiobox Implementation
 * ------------------------------------------------------------------------- */
    RadioGroup = Class(Widget);

    RadioGroup.prototype.constructor = function (args) {
        if (!args)
            throw MSG_INVALID_ARGUMENT_LIST;

        this.private("id", args.id);
        this.private("label", args.label || "");
        this.private("childs", new Array());

        this.configureRadioboxes(args.target);
    };

    RadioGroup.prototype.configureRadioboxes = function (targetIds) {
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
 *  Grid Implementation
 * ------------------------------------------------------------------------- */
    Grid = Class(Widget);
    
    Grid.prototype.constructor = function (args) {
        if (!args)
            throw MSG_INVALID_ARGUMENT_LIST;

        this.private("id", args.id);
        this.private("label", args.label || "");
        this.private("data");
        this.private("columns");
        this.private("rows");
        this.private("selectedItems", null);

        this.configureGrid();

        this.setColumns( args.columns || new Array() );
        this.setData( args.data || new Array() );
        
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

    	var target = {
			grid: grid,
			gridHeader: gridHeader,
			gridBody: gridBody,
			gridHolder: gridHolder
		};

    	this.private("target", target);
    };
    
    Grid.prototype.setData = function (data) {
    	if (!data) return;
    	this.clean();
    	
    	for (var i=0; i<data.length; i++)
    		this.addRow (data[i], i);
    	
    	this.data = data;
    };
    
    Grid.prototype.clean = function () {
    	var body = this.target.gridBody;
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
    	var header = this.target.gridHeader;
    	var column = new GridColumn(columnData, this).render();
		header.appendChild(column);
    };
    
    Grid.prototype.addRow = function (rowData) {
    	var rowPos = this.rows.length;
    	var row = new GridRow(rowData, rowPos, this);
    	this.rows[rowPos] = row;
    	var renderedRow = row.render();
    	EventHandler.addEventListener(renderedRow, "click",
				this.getMethod("selectRow", row));
    	this.target.gridBody.appendChild (renderedRow);
    };
    
    Grid.prototype.getRow = function (index) {
    	return this.rows[index];
    };

    Grid.prototype.selectRow = function (event, gridRow) {
    	for (var i=0; i<this.rows.length; i++) {
    		this.rows[i].target.className = 
    			this.rows[i].target.className.replace("selected", "");
    	}
    	gridRow.target.className+= " selected";
    	this.selectedItems = [gridRow];
    	this.flush();
    };

    Grid.prototype.flush = function () {
    	var buffer = "";
    	for (var selectedItemIndex in this.selectedItems) {
    		var item = this.selectedItems[selectedItemIndex].value;
    		if (!item)
    			continue;
    		buffer+= "{";
    		for (var attr in item) {
    			buffer+= attr + ":'" + escape(item[attr]) + "',";
    		}
    		buffer+= "},";
    	}
    	this.target.gridHolder.value = buffer;
    };

    Grid.prototype.setWidth = function (width) {
    	if (!width)
    		return;
    	this.target.grid.style.width = width;
    };
    
    Grid.prototype.setHeight = function (height) {
    	if (!height)
    		return;
    	this.target.gridBody.style.height = height;
    };

/* -------------------------------------------------------------------------
 *  GridColumn
 * ------------------------------------------------------------------------- */
    GridRow = Class();
    GridRow.prototype.constructor = function (values, rowPos, parent) {
    	this.private ("parent", parent);
    	this.private ("id", parent.id + "GridRow" + rowPos);
    	this.private ("value", values);
    	this.private ("highlighted", rowPos % 2 == 0);
    	this.private ("selected", false);
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
    			value: this.value[headerColumn.id],
    			width: headerColumn.width
    		};
    		
    		var renderedColumn = new ColumnClass(columnData, this).render();
    		
    		target.appendChild ( renderedColumn );
    	}
    	this.target = target;
    	return target;
    };
    
    GridRow.prototype.flush = function () {
    	this.parent.flush();
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
    };

    GridColumn.prototype.render = function () {
    	var column = document.createElement("li");
		column.appendChild(document.createTextNode(this.value));
		
		//alert("parent: " + this.parent);
		
		column.setAttribute("id", this.parent.id + "Column" + this.id);

		if (this.width)
			column.setAttribute("style", "width:" + this.width);
		return column;
    };
    

/* -------------------------------------------------------------------------
 *  GridColumnFactory
 * ------------------------------------------------------------------------- */
    GridColumnFactory = {
    	registeredColumnTypes: {defaultColumn: GridColumn},
    	register: function (columnType, clazz){
    		GridColumnFactory.registeredColumnTypes[
                GridColumnFactory.registeredColumnTypes.lenght] = clazz;
    	}
    };
