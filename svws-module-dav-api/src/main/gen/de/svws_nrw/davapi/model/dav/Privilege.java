package de.svws_nrw.davapi.model.dav;

import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "content"
})
@XmlRootElement(name = "privilege")
public class Privilege {
    @XmlMixed
    @XmlAnyElement(lax = true)
	private List<Object> content;

	public List<Object> getContent() {
		if (content==null)
			content = new ArrayList<>();
		return content;
	}

	public void setContent(List<Object> content) {
		this.content = content;
	}

}
