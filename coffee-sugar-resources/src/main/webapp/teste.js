/*Class = function (SuperClass) {

    var clazz = function () {
        if (arguments.length >0 && arguments[0] == Class.prototype.__classInstance__)
            return;
        if (this.constructor)
            this.constructor.apply(this, arguments);
    };
    if (!SuperClass) SuperClass = Object;
    clazz.prototype = new SuperClass(Class.prototype.__classInstance__);
    clazz.prototype.superClass = SuperClass;
    clazz.prototype.toString = SuperClass.prototype.toString;
    clazz.prototype.valueOf = SuperClass.prototype.valueOf;
    return clazz;
};
Class.prototype.__classInstance__ = "Class.prototype.__classInstance__";*/

Radiobox = Class();
Radiobox.prototype.constructor = function (args) {
    if (!args.target && args.id)
        args.target = document.getElementById(args.id);

    if (!args.target)
        throw MSG_INVALID_ID_ARGUMENT;

    this.target =args.target;
    this.label = args.label || args.id;
    this.required = args.required;
};

Radiobox.prototype.isChecked = function () {
    return (this.target.checked == "checked" || this.target.checked == true);
};

Radiobox.prototype.setChecked = function (value) {
    this.target.checked = (value == "checked" || value == true) ? "checked" : "";
};
Radiobox.prototype.getValue = function () {
    return this.target.value;
};

RadioGroup = Class();

RadioGroup.prototype.constructor = function (args) {
    if (!args)
        throw MSG_INVALID_ARGUMENT_LIST;

    this.id = args.id;
    this.childs = new Array();
    this.configureRadioboxes(args.target);
};

RadioGroup.prototype.addChild = function () {
    for (var i=0; i<arguments.length; i++) {
        var child = arguments[i];
        this.childs[this.childs.length] = child;
        if (child.id)
            this[child.id] = child;
    }
};

RadioGroup.prototype.configureRadioboxes = function (targetIds) {
    for (var i=0; i<targetIds.length; i++) {
        var radioId = targetIds[i];
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


