/*
 * Copyright 2011 Miere Liniel Teixeira
 *
 * Especial Thanks to Marcos Augusto Garcia for his contribuition for the API.
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
 * Inheritable, Non Static and Functional Class Implementation
 * @param SuperClass: the target Superclass
 * ------------------------------------------------------------------------- */
    Class = function (SuperClass) {

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
        clazz.prototype.private = Class.prototype.private;
        clazz.prototype.getMethod = Class.prototype.wrapMethod;
        return clazz;
    };

    Class.prototype.__classInstance__ = "Class.prototype.__classInstance__";

    Class.prototype.private = function (field, defaultValue) {
        this[field] = defaultValue;
        var name = field[0].toUpperCase() + field.substring(1);
        if (!this["set" + name])
            this["set" + name] = function (value) {
                this[field] = value;
            };
        if (!this["get" + name])
            this["get" + name] = function (value) {
                return this[field];
            };
    };

	Class.prototype.wrapMethod = function () {
        var self = this;
		var method = self[arguments[0]];
		var methodArguments = arguments;
		return function (event) {
			methodArguments[0] = event;
			return method.apply(self, methodArguments);
		};
    };

/* -------------------------------------------------------------------------
 * 
 * ------------------------------------------------------------------------- */
    Array.prototype.foreach = function(method) {
	    for (var i=0; i<this.length; i++)
		    method(this[i], i);
    };

    Array.prototype.size = function () {
	    return this.length;
    };

   Array.prototype.append = function (item) {
	    this[this.length] = item;
    };

/* -------------------------------------------------------------------------
 * 
 * ------------------------------------------------------------------------- */
    EventHandler = {
        addEventListener: function(target, eventName, callback) {
	        if (!target) throw "addEventListener: null target";
	        if (target.attachEvent){
		        target.addEventListener = target.attachEvent;
		        if (eventName.indexOf("on") != 0)
			        eventName = "on" + eventName;
	        }
	        if (!target.addEventListener)
		        throw "addEventListener: bad target";
	        target.addEventListener(eventName,
                    function(e){
			            if (!e) e = window.event;
			            callback(e);
		            }, null);
        }
    };

/* -------------------------------------------------------------------------
 * 
 * ------------------------------------------------------------------------- */
	var $node = function(name) {
		return new document.createElement(name);
	};

	var $text = function(text) {
		return document.createTextNode(text);
	};

	var $map = function (sequence, method) {
		var newSequence = new Array();
		for (var i=0; i<sequence.length; i++)
			newSequence[i] = method(sequence[i]);
		return newSequence;
	};

	var $dir = function (obj) {
		buffer = "";
		for (var attr in obj)
			buffer += attr + ":" + obj[attr] + ", \n";
		return buffer;
	};


