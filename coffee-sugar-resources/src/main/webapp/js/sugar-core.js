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
            this.events = new Object();
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
        clazz.prototype.event = Class.prototype.event;
        clazz.prototype.dispatch = Class.prototype.dispatch;
        return clazz;
    };

    Class.prototype.__classInstance__ = "Class.prototype.__classInstance__";

    Class.prototype.private = function (field, defaultValue) {
        this[field] = defaultValue;
        var name = field.charAt(0).toUpperCase() + field.substring(1);
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
		var args = new Array();
		for (var i=1; i<arguments.length; i++)
			args.append(arguments[i]);
		return function () {
			for (var i=0; i<arguments.length; i++)
				args.append(arguments[i]);
			return method.apply(self, args);
		};
    };

    Class.prototype.event = function (eventName, callback) {
    	if (!this.events[eventName])
    		this.events[eventName] = new Array();
    	this.events[eventName].append(callback);
    };

    Class.prototype.dispatch = function (eventName) {
    	var events = this.events[eventName];
    	if (!events)
    		return;

    	var args = new Array();
    	for (var i=1; i<arguments.length; i++)
			args.append(arguments[i]);

    	for (var i=0; i<events.size(); i++) {
    		events[i].apply(window, args);
    	}
    };

/* -------------------------------------------------------------------------
 * HttpRequest
 * ------------------------------------------------------------------------- */
	HttpRequest = Class();
	HttpRequest.prototype.toString = function () { return "HttpRequest Object"; };

	HttpRequest.prototype.constructor = function (url, method) {
		this.xmlHttp = null;
		this.method = method || "GET";
		try {
		    // Firefox, Opera 8.0+, Safari, Camino, Chrome
		    this.xmlHttp = new XMLHttpRequest();
		}
		catch(e) {
		    // Internet Explorer
		    try {
		        this.xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
		    } catch(e) {
		        this.xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		    }
		}
		this.xmlHttp.open(this.method, url, true);
	};

	HttpRequest.prototype.send = function (content) {
		var oXHttp = this.xmlHttp;
		this.xmlHttp.onreadystatechange = this.getMethod("responseHandler");
		this.xmlHttp.send( content );
	};
	
	HttpRequest.prototype.responseHandler = function () {
		var http = this.xmlHttp;
		if (http.status != "200" && http.status != 200)
			throw "HTTP ERROR: " + http.statusText;

		var state = ["uninitialized", "loading", "loaded", "interactive", "ready"];

		this.dispatch(
		   state[http.readyState],
		   http.responseText);
	};

	HttpRequest.prototype.addHttpHeader = function (header, value) {
		this.xmlHttp.setRequestHeader(header, value);
	};

	HttpRequest.prototype.getHttpHeader = function (header) {
		this.xmlHttp.getRequestHeader(header);
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
    
    Array.prototype.contains = Array.prototype.contains || function (value) {
    	for (var i=0; i<this.length; i++)
    		if (this[i] == value)
    			return true;
    	return false;
    };
    
    Array.prototype.remove = Array.prototype.remove || function (object) {
    	var idx = this.indexOf (object);
    	return this.removeAt (idx);
    };

    Array.prototype.removeAt = function (idx) {
    	return this.splice(idx, 1);
    };

    Array.prototype.indexOf = Array.prototype.indexOf || function(obj){
        for(var i=0; i<this.length; i++){
            if(this[i]==obj){
                return i;
            }
        }
        return -1;
    };

/* -------------------------------------------------------------------------
 * 
 * ------------------------------------------------------------------------- */
    String.prototype.contains = function (str) {
    	return this.indexOf(str) >=0;
    };

    String.prototype.startsWith = function (str) {
    	return this.indexOf(str) ==0;
    };

/* -------------------------------------------------------------------------
 * 
 * ------------------------------------------------------------------------- */
    DomUtil = {
	    applyOpacity: function (target, opacity) {
	    	if (!target) return;

	    	target.style.opacity = opacity/100;
	    	if (navigator.appName.indexOf("Netscape")!=-1&&parseInt(navigator.appVersion)>=5)
	    		target.style.MozOpacity=opacity/100;
	    	else if (navigator.appName.indexOf("Microsoft")!=-1&&parseInt(navigator.appVersion)>=4)
	    		target.filters.alpha.opacity=opacity;
	    },
	    fadeIn: function (target, opacity, initialOpacity) {
	    	opacity = opacity || 50;
	    	var speed = (opacity > 50 ) ? 5 : 10;
	    	initialOpacity = initialOpacity || 0;
	    	for (var i=initialOpacity; i<(opacity+1); i++) {
	    		(function(pos){
	    			setTimeout(function (){
	    				DomUtil.applyOpacity (target, pos)
					},pos*speed);
	    		})(i);
	    	}
	    },
	    getWindowWidth: function (){
	    	var html = document.getElementsByTagName("html")[0];
	    	return Math.max(html.scrollWidth, window.innerWidth);
	    },
	    getWindowHeight: function (){
	    	var html = document.getElementsByTagName("html")[0];
	    	return Math.max(html.scrollHeight, window.innerHeight);
	    },
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


