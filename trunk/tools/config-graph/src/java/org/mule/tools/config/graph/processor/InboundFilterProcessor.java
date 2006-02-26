package org.mule.tools.config.graph.processor;

import com.oy.shared.lm.graph.Graph;
import com.oy.shared.lm.graph.GraphEdge;
import com.oy.shared.lm.graph.GraphNode;
import org.jdom.Element;
import org.mule.tools.config.graph.config.ColorRegistry;
import org.mule.tools.config.graph.config.GraphConfig;
import org.mule.tools.config.graph.util.MuleTag;

public class InboundFilterProcessor extends TagProcessor {

	
	public InboundFilterProcessor(GraphConfig config) {
		super(config);
		
	}

	public void processInboundFilter(Graph graph, Element endpoint,
			GraphNode endpointNode, GraphNode parent) {
		Element filter = endpoint.getChild(MuleTag.ELEMENT_FILTER);
		boolean conditional = false;

		if (filter == null) {
			filter = endpoint.getChild(MuleTag.ELEMENT_LEFT_FILTER);
			conditional = filter != null;
		}

		if (filter != null) {

			GraphNode filterNode = graph.addNode();
			filterNode.getInfo().setHeader(
					filter.getAttributeValue(MuleTag.ATTRIBUTE_CLASS_NAME));
			filterNode.getInfo().setFillColor(ColorRegistry.COLOR_FILTER);
			StringBuffer caption = new StringBuffer();
			appendProperties(filter, caption);
			filterNode.getInfo().setCaption(caption.toString());
			// this is a hack to pick up and/or filter conditions
			// really we need a nice recursive way of doing this
			if (conditional) {
				filter = endpoint.getChild(MuleTag.ELEMENT_RIGHT_FILTER);
				GraphNode filterNode2 = graph.addNode();
				filterNode2.getInfo().setHeader(
						filter.getAttributeValue(MuleTag.ATTRIBUTE_CLASS_NAME));
				filterNode2.getInfo().setFillColor(ColorRegistry.COLOR_FILTER);
				StringBuffer caption2 = new StringBuffer();
				appendProperties(filter, caption2);
				filterNode2.getInfo().setCaption(caption2.toString());
				graph.addEdge(endpointNode, filterNode2).getInfo().setCaption(
						"filters on");
			}
			processInboundFilter(graph, filter, filterNode, parent);

			graph.addEdge(endpointNode, filterNode).getInfo().setCaption(
					"filters on");
		} else {
			GraphEdge e = graph.addEdge(endpointNode, parent);
            e.getInfo().setCaption("in");
            if("true".equalsIgnoreCase(endpoint.getAttributeValue(MuleTag.ATTRIBUTE_SYNCHRONOUS))) {
                e.getInfo().setArrowTailNormal();
            }
		}
	}

}
