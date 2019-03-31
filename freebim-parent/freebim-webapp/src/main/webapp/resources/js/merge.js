/**
 * 
 */
at.freebim.db.merge = {
		
		
		merge : function (a, b) {
			var db = at.freebim.db, nf = db.domain.NodeFields;
			if (a && b) {
				if (a[nf.CLASS_NAME] == b[nf.CLASS_NAME]) {
					var m = db.merge, c = m.copyField; 
					m.merges(a, b);
					c(a, b, nf.NAME);
					c(a, b, nf.NAME_EN);
					c(a, b, nf.DESC);
					c(a, b, nf.DESC_EN);
					c(a, b, nf.CODE);
					c(a, b, nf.BSDD_GUID); // bsDD-Guid
				}
			}
		},
		
		merges : function (a, b) {
			var db = at.freebim.db, rf = db.domain.RelationFields, nf = db.domain.NodeFields, 
				clazz = db.domain[a[nf.CLASS_NAME]], rels = clazz.relations, i, n = rels.length, r;
			for (i=0; i<n; i++) {
				r = rels[i]; // i.E. {field: "eq", 		t: 9, 	cn: "Equals",		dir: "BOTH" }
				db.merge.mergeRels(r[rf.FIELD_NAME], a, b, r[rf.DIRECTION]);
			}
		},
		
		mergeRels : function (r, a, b, dir) {
			var d = at.freebim.db.domain, i, j, k, m, n, tmp = {}, keys, res = [], rel, s, t, found,
				rf = d.RelationFields, nf = d.NodeFields;
			n = ((a[r]) ? a[r].length : 0);
			// store all relations from a in tmp, key is id of target node.
			for (i=0; i<n; i++) {
				t = a[r][i];
				s = ((a[nf.NODEID] == t[rf.FROM_NODE]) ? t[rf.TO_NODE] : t[rf.FROM_NODE]);
				tmp[s] = t;
				res.push(t);
			}
			// for all relations of b
			n = ((b[r]) ? b[r].length : 0);
			for (i=0; i<n; i++) {
				t = b[r][i];
				s = ((b[nf.NODEID] == t[rf.FROM_NODE]) ? t[rf.TO_NODE] : t[rf.FROM_NODE]);
				
				// target node of b is not in relations of a,
				// so create a new relation from a to that target node
				rel = { id: null };
				if (dir == "IN") {
					rel[rf.FROM_NODE] = s;
					rel[rf.TO_NODE] = a[nf.NODEID];
				} else {
					rel[rf.FROM_NODE] = a[nf.NODEID];
					rel[rf.TO_NODE] = s;
				}

				// omit self circular relations because
				// (b)-[:r]->(a) would result in (a)-[:r]->(a)
				if (rel[rf.FROM_NODE] == rel[rf.TO_NODE]) {
					continue;
				}
				
				// copy all fields of that b relation to the new relation
				keys = Object.keys(t);
				m = keys.length;
				for (j=0; j<m; j++) {
					k = keys[j];
					if (k != rf.ID && k != rf.FROM_NODE && k != rf.TO_NODE) {
						rel[k] = t[k];
					}
				}

				// don't add duplicate relations
				// (a)-[rel1]->(x), (b)-[rel2]->(x), rel1 == rel2
				found = false;
				for (j=0; j<res.length; j++) {
					if (at.freebim.db.merge.isEqualRel(rel, res[j], dir)) {
						found = true;
						break;
					}
				}
				if (!found) {
					res.push(rel);
				}
			}
			a[r] = res;
		},
		
		isEqualRel : function (r, s, dir) {
			var rt, db = at.freebim.db, d = db.domain, rf = d.RelationFields;
			if (r && s) {
				rt = r[rf.TYPE];
				if (rt == d.RelationTypeEnum.OF_UNIT && dir == "OUT") {
					// (a:Measure)-[:OF_UNIT]-(b:Unit) only once!
					return true;
				}
				if (rt == d.RelationTypeEnum.OF_DATATYPE && dir == "OUT") {
					// (a:Measure)-[:OF_DATATYPE]-(b:DataType) only once!
					return true;
				}
				if (rt == d.RelationTypeEnum.HAS_VALUE && dir == "OUT") {
					// (a:Measure)-[:HAS_VALUE]-(b:ValueList) only once!
					return true;
				}

				var i, k, keys = Object.keys(r), n = keys.length, sKeys = Object.keys(s), m = sKeys.length;
				if (n != m) {
					return false;
				}
				for (i=0; i<n; i++) {
					k = keys[i];
					
					if (k == rf.ID || k == rf.ORDERING) {
						// ignore non-equality for relation-ID and ordering 
						continue;
					}
					if (rt == d.RelationTypeEnum.REFERENCES && k == rf.TIMESTAMP) {
						// ignore timestamp non-equality for REFERENCES relations (without refId and refIdName)
						// this prevents multiple Library references without actual data
						continue;
					}
					if ((s[k] == undefined) || (r[k] != s[k])) {
						return false;
					}
				}
				return true;
			}
		},
		
		copyField : function (a, b, field) {
			if (!a[field] && b[field]) {
				a[field] = b[field];
			}
		}
};