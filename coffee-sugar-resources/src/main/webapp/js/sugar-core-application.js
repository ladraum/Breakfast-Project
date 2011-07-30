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

/* -------------------------------------------------------------------------
 * Application
 * ------------------------------------------------------------------------- */
    Application = Class();

    Application.prototype.constructor = function () {
        this.private ("children", new Array());
        this.private ("widgets", new Array());
        this.private ("validators", new Array());
    };

    Application.prototype.addChild = function (child) {
        if (!child instanceof Component)
            throw MSG_INVALID_ARGUMENT + "'child' argument must be from a 'Component'.";
        this.children.append(child);
        if (child instanceof Widget)
            this.widgets.append(child);
        if (child.id)
            this[child.id] = child;
    };

    Application.prototype.registerObject = function (name, value) {
    	if (this[name])
    		this.showMessage("[Development failure] Duplicated id '" + name + "'.")
    	this[name] = value;
    };

    Application.prototype.addValidator = function (validator) {
        if (!validator instanceof Validator)
            throw MSG_INVALID_ARGUMENT + "'validator' argument must be from a 'Validator'.";
        this.validators.append(validator);
    };

    Application.prototype.validate = function () {
        try {
            for (var i=0; i<this.validators.length; i++) {
                var validator = this.validators[i];
                if (!validator.validate(this.widgets)) {
                    this.onValidationFail(validator);
                    return false;
                }
            }
            this.onValidationSuccess();
            return true;
        } catch (e) {
            alert("ERROR: " + e);
            return false;
        }
    };
    
    Application.prototype.showMessage = function (message) {
    	alert(message);
    };

    Application.prototype.onValidationFail = function (validator) {
        this.showMessage(validator.getErrorMessage());
    };

    Application.prototype.onValidationSuccess = function () {
    };
    
    Application.prototype.submit = function () {
    	try {
    		if (this.validate() && this.widgets.length> 0) {
	    		var form = document.getElementById("sugarApplicationForm");
	    		form.submit();
	    	}
    	} catch (e) {
            this.showMessage("ERROR: " + e);
    	}
    };
    
    Application.prototype.redirect = function (url) {
    	document.location.href=url;
    };

/* -------------------------------------------------------------------------
 * Validator
 * ------------------------------------------------------------------------- */
    Validator = Class();

    Validator.prototype.constructor = function () {
        this.private("errorMessage","");
        this.private("target",null);
    };

    Validator.prototype.flush = function () {
        this.setErrorMessage("");
        this.setTarget(null);
    };

    Validator.prototype.validate = function (widgets) {
        return true;
    };

/* -------------------------------------------------------------------------
 * RequiredFieldsValidator
 * ------------------------------------------------------------------------- */
    RequiredFieldsValidator = Class(Validator);

    RequiredFieldsValidator.prototype.validate = function (widgets) {
        for (var i=0; i<widgets.length; i++) {
            var widget = widgets[i];
            if (!widget instanceof Widget)
                throw MSG_INVALID_ARGUMENT + "'widgets' param must contains only Widgets instances.";
            if (widget.isRequired() && !widget.getValue()) {
                var msg = "Campo '" + widget.getLabel() + "' é de preenchimento obrigatório.";
                this.setErrorMessage(msg);
                this.setTarget(widget);
                widget.focus();
                return false;
            }
        }
        return true;
    };

