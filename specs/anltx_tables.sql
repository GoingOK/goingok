CREATE TABLE anltx_author_graphs (
    graph_id integer NOT NULL REFERENCES anltx_graphs(graph_id) ON DELETE CASCADE ON UPDATE CASCADE,
    goingok_id uuid NOT NULL REFERENCES users(goingok_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE anltx_charts (
    chart_id SERIAL PRIMARY KEY,
    name text,
    description text,
    graph_id integer REFERENCES anltx_graphs(graph_id) ON DELETE SET NULL ON UPDATE CASCADE
);


CREATE UNIQUE INDEX anltx_charts_pkey ON anltx_charts(chart_id int4_ops);

CREATE TABLE anltx_edge_labels (
    chart_id integer REFERENCES anltx_charts(chart_id) ON DELETE CASCADE ON UPDATE CASCADE,
    edge_id integer REFERENCES anltx_edges(edge_id) ON DELETE CASCADE ON UPDATE CASCADE,
    label_id integer REFERENCES anlytx_labels(label_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE anltx_edges (
    edge_id SERIAL PRIMARY KEY,
    edge_type text NOT NULL,
    directional boolean NOT NULL DEFAULT false,
    weight numeric NOT NULL DEFAULT 0.0,
    graph_id integer REFERENCES anltx_graphs(graph_id) ON DELETE CASCADE ON UPDATE CASCADE,
    source_node_id integer REFERENCES anltx_nodes(node_id) ON DELETE CASCADE ON UPDATE CASCADE,
    target_node_id integer REFERENCES anltx_nodes(node_id) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE UNIQUE INDEX anltx_edges_pkey ON anltx_edges(edge_id int4_ops);

CREATE TABLE anltx_graphs (
    graph_id SERIAL PRIMARY KEY,
    graph_type text,
    name text,
    description text
);


CREATE UNIQUE INDEX anltx_graphs_pkey ON anltx_graphs(graph_id int4_ops);

CREATE TABLE anltx_group_graphs (
    graph_id integer REFERENCES anltx_graphs(graph_id) ON DELETE CASCADE ON UPDATE CASCADE,
    group_code text REFERENCES group_codes(group_code) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE anltx_node_labels (
    chart_id integer REFERENCES anltx_charts(chart_id) ON DELETE CASCADE ON UPDATE CASCADE,
    node_id integer REFERENCES anltx_nodes(node_id) ON DELETE CASCADE ON UPDATE CASCADE,
    label_id integer REFERENCES anlytx_labels(label_id) ON DELETE CASCADE ON UPDATE CASCADE,
    expression text
);

CREATE TABLE anltx_nodes (
    node_type text NOT NULL DEFAULT ''::text,
    start_idx integer NOT NULL,
    end_idx integer NOT NULL,
    ref_id integer REFERENCES reflections(ref_id) ON DELETE CASCADE ON UPDATE CASCADE,
    node_id integer DEFAULT nextval('nltx_nodes_node_id_seq'::regclass) PRIMARY KEY
);


CREATE UNIQUE INDEX nltx_nodes_pkey ON anltx_nodes(node_id int4_ops);

CREATE TABLE anlytx_graph_nodes (
    node_id integer REFERENCES anltx_nodes(node_id) ON DELETE CASCADE ON UPDATE CASCADE,
    graph_id integer REFERENCES anltx_graphs(graph_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE anlytx_labels (
    label_id SERIAL PRIMARY KEY,
    label_type text NOT NULL,
    ui_name text,
    description text,
    selected boolean NOT NULL DEFAULT false,
    properties json
);


CREATE UNIQUE INDEX anlytx_labels_pkey ON anlytx_labels(label_id int4_ops);
