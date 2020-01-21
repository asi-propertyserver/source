at.freebim.db.websocket = {
	
	stompClient : null,
	
	uuidv4 : function () {
		return ([1e7]+-1e3+-4e3+-8e3+-1e11).replace(/[018]/g, c =>
			(c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
		);
	},
	
	connect : function (callback) {
		var self = at.freebim.db.websocket,
			s = at.freebim.db.websocketsettings,
			socket = new SockJS(s.endpoint);
		self.uuid = at.freebim.db.websocket.uuidv4();
		self.stompClient = window.Stomp.over(socket);
		self.stompClient.connect({}, function (frame) {
			console.log('Connected: ' + frame);
			self.stompClient.subscribe(s.clientprefix + 'init', function (rs) {
				console.log(s.clientprefix + 'init: ' + (JSON.parse(rs.body).uuid === self.uuid));
			});
			self.stompClient.subscribe(s.clientprefix + 'ping', function (rs) {
				console.log(s.clientprefix + 'ping: ' + rs.body);
			});
			self.stompClient.send(s.serverprefix + "hello", {}, JSON.stringify({ 'uuid' : self.uuid }));
			if (callback) {
				callback();
			}
		});
	},
	disconnect : function () {
		var self = at.freebim.db.websocket;
		if (self.stompClient !== null) {
			self.stompClient.disconnect();
			self.stompClient = null;
		}
		console.log("Disconnected");
	},
	subscribe : function (what, callback) {
		var self = at.freebim.db.websocket, 
			s = at.freebim.db.websocketsettings;
		return self.stompClient.subscribe(s.clientprefix + what, function (rs) {
			if (rs) {
				rs = JSON.parse(rs.body);
				if (rs) {
					console.log(s.clientprefix + what + ': ok.');
					callback(rs);
					return;
				}
			}
			console.log(s.clientprefix + what + ': failed');
		});
	},
	send : function (address, data, target) {
		var self = at.freebim.db.websocket, 
			s = at.freebim.db.websocketsettings;
		self.stompClient.send(s.serverprefix + address, {}, JSON.stringify({
			'uuid' : self.uuid, 
			data : data,
			target : s.clientprefix + target
		}));
	}

};