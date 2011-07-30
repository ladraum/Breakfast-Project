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
        
        if (!clazz.prototype.event)
        	clazz.prototype.event = Class.prototype.event;
        if (!clazz.prototype.dispatch)
        	clazz.prototype.dispatch = Class.prototype.dispatch;
        if (!clazz.prototype.cleanEvents)
        	clazz.prototype.cleanEvents = Class.prototype.cleanEvents;
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

    	for (var i=0; i<events.size(); i++)
    		events[i].apply(window, args);
    };
    
    Class.prototype.cleanEvents = function () {
    	this.events = new Object();
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
		if (http.readyState == 4 && http.status != "200" && http.status != 200) {
			this.dispatch("error", http.statusText);
			//throw "HTTP ERROR: " + http.statusText;
			return;
		}

		var state = ["uninitialized", "loading", "loaded", "interactive", "ready"];

		this.dispatch(
		   state[http.readyState],
		   http.readyState ==4 ? 
			   http.responseText : null);
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
    	return this;
    };

    Array.prototype.size = function () {
    	return this.length;
    };
    
    Array.prototype.addAll = function (list) {
    	for (var i=0;i<list.length; i++)
    		this.append(list[i]);
    	return this;
    };

    Array.prototype.append = Array.prototype.append || function (item) {
    	this[this.length] = item;
    	return this;
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
	    applyOpacity: function (target, opacity, timeout) {
	    	if (!target) return;
	    	target = target.target || target;

	    	timeout = timeout || 1;
	    	setTimeout(function(){
		    	target.style.opacity = opacity/100;
		    	target.style.filter = 'alpha(opacity=' + opacity + ')';
		    	//target.style.display = (opacity > 0) ? "" : "none";
	    	}, timeout);
	    },

	    fade: function (target, initialOpacity, opacity, callback) {
	    	if (!target) return;

	    	target = target.target || target;
	    	initialOpacity = initialOpacity || 0;
	    	opacity = !opacity && opacity != 0 ? 100 : opacity;
	    	var speed = (opacity > 50 ) ? 5 : 10;
	    	var max = Math.abs(opacity - initialOpacity) * speed;

	    	$range(initialOpacity, opacity, function (currentOpacity){
	    		var currentTimeout = currentOpacity*speed;
	    		if (initialOpacity > opacity)
	    			currentTimeout = max - currentTimeout;
	    		DomUtil.applyOpacity (target, currentOpacity,
	    				currentTimeout);
	    	});

	    	if (callback)
	    		setTimeout(callback, Math.max(opacity, initialOpacity)*speed);
	    },

	    resizeHeight: function (target, endHeight, startHeight, callback) {
	    	if (!target) return;
	    	target = target.target || target;
	    	
	    	if (!startHeight && startHeight != 0)
	    		startHeight = parseInt(DomUtil.getStyle(target, "clientHeight")) || 0;
	    	endHeight = parseInt(endHeight);
	    	var range = Math.abs(endHeight - startHeight);
	    	var speed = range < 500 ? 2 : 1;
	    	if (startHeight < endHeight)
	    		for (var i=0; i<=range; i++)
	    			DomUtil.setHeight(target, (i+startHeight) + "px", i * speed);
	    	else
	    		for (var i=range; i>=0; i--)
	    			DomUtil.setHeight(target,(i+endHeight) + "px", (range - i) * speed);
	    	if (callback)
	    		setTimeout(callback, range*speed);
	    },

	    getWidth: function (target) {
	    	if (!target) return;
	    	target = target.target || target;

	    	return target.display.width;
	    },
	    setWidth: function (target, width, timeout) {
	    	if (!target) return;
	    	target = target.target || target;
	    	
	    	if (typeof(width) == "number")
	    		width+= "px";

	    	timeout = timeout || 1;
	    	setTimeout(function(){
	    		target.style.width = width;
	    	}, timeout);
	    },

	    getHeight: function (target) {
	    	if (!target) return;
	    	target = target.target || target;

	    	return target.display.height;
	    },
	    setHeight: function (target, height, timeout) {
	    	if (!target) return;
	    	target = target.target || target;
	    	
	    	if (typeof(height) == "number")
	    		height+= "px";

	    	timeout = timeout || 1;
	    	setTimeout(function(){
	    		target.style.height = height;
	    	}, timeout);
	    },

	    getStyle: function (target,styleProp) {
	    	if (!target) return;
	    	target = target.target || target;

	    	if (target.currentStyle)
	    		var y = target.currentStyle[styleProp];
	    	else if (window.getComputedStyle)
	    		var y = document.defaultView.getComputedStyle(target,null).getPropertyValue(styleProp);
	    	return y;
	    },

	    getWindowWidth: function (){
	    	var html = document.getElementsByTagName("html")[0];
	    	return Math.max(html.scrollWidth || 0, window.innerWidth || 0);
	    },
	    getWindowHeight: function (){
	    	var html = document.getElementsByTagName("html")[0];
	    	return Math.max(html.scrollHeight || 0, window.innerHeight || 0);
	    },

        addEventListener: function(target, eventName, callback) {
        	if (!target) return;
	    	target = target.target || target;

	        if (!target) throw "addEventListener: null target";
	        if (target.attachEvent){
		        target.addEventListener = target.attachEvent;
		        if (eventName.indexOf("on") != 0)
			        eventName = "on" + eventName;
	        }
	        if (!target.addEventListener)
		        throw "addEventListener: bad target";
	        target.addEventListener(eventName, callback, null);
        },
        removeEventListener: function(target, eventName, callback) {
        	if (!target) return;
	    	target = target.target || target;

	        if (!target) throw "addEventListener: null target";
	        if (target.detachEvent){
		        target.removeEventListener = target.detachEvent;
		        if (eventName.indexOf("on") != 0)
			        eventName = "on" + eventName;
	        }
	        if (!target.removeEventListener)
		        throw "addEventListener: bad target";
	        target.removeEventListener(eventName,
                    callback, null);
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

	var $range = function (start, end, callback) {
        var i= start;
        while((start <=end && i<=end)||(start > end && i>=end)){
            callback(i);
            if (start<=end) i++; else i--;
        }
    };
