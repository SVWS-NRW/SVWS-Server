package de.svws_nrw.test.davapitests.util.xml;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import groovy.xml.XmlSlurper;
import io.restassured.path.xml.XmlPath;

/**
 * Diese Klasse basiert auf dem {@link XmlPath} und überschreibt
 * {@link XmlPath#get(String)}, so dass immer vom zuletzt betrachteten
 * XML-Element ausgegangen wird. Dazu bietet sie die {@link #up()} und
 * {@link #up(int)} Methoden, um den im XML-Tree nach oben zu wandern.
 *
 * Es werden Methoden aus XmlPath zusätzlich als *AndUp angeboten, um auf ein
 * Element zu peeken.
 *
 * Darüber hinaus gibt es asserts (bspw. für die Existenz von Elementen), welche
 * in Zusammenhang mit dem XMLPath zugrundeliegenden {@link XmlSlurper} zu
 * unnötig viel, schlecht lesbarem Code beim Testen führen würden.
 */
public class XmlPathWalker extends XmlPath {

	public XmlPathWalker(final String string) {
		super(string);
	}

	private String node = "";

	public String getNode() {
		return this.node;
	}

	public XmlPathWalker up() {
		int idx = node.lastIndexOf('.');
		if (idx > 0) {
			this.node = node.substring(0, idx);
		}
		return this;
	}

	@Override
	public <T> T get(String path) {
		if (node == null || node.isBlank()) {
			this.node = path;
		} else {
			this.node += "." + path;
		}
		return super.get(this.node);
	}

	public <T> T getNodeUp(int steps) {
		for (int i = 0; i < steps; i++) {
			up();
		}
		return this.get(node);
	}

	public void backToDocRoot() {
		this.node = "";
	}

	public boolean nodeExistsAndUp(String path) {
		var nodeBuff = node;
		var res = !getBoolean(path + ".isEmpty()");
		this.node = nodeBuff;
		return res;
	}

	public String getStringAndUp(String string) {
		String res = getString(string);
		up();
		return res;
	}

	public int getIntAndUp(String string) {
		int res = getInt(string);
		up();
		return res;
	}

	public TypeSafeMatcher<String> nodeExists() {
		return new TypeSafeMatcher<>() {

			@Override
			protected boolean matchesSafely(final String item) {
				return XmlPathWalker.this.nodeExistsAndUp(item);
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("node existing");
			}
		};
	}
}
