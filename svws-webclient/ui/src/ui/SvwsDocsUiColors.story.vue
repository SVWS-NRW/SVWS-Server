<template>
	<Story title="Farben" id="farben" group="top" icon="ri:palette-line" :responsive-disabled="true" :layout="{ type: 'single', iframe: false }" auto-props-disabled>
		<Variant title="Einleitung" source=" " id="resources">
			<div class="htw-prose dark:htw-prose-invert inline">
				<h1>Einleitung</h1>
				<p class="text-ui">
					SVWS-UI bietet einen breite Palette von aufeinander abgestimmten Farben, die für viele Anwendungsfälle vordefiniert sind und den Richtlinien
					zu Kontrasten nach WCAG entsprechen. Die Benennung der Farben entsprechen einer semantischen Struktur, die die Wahl der passenden Farbe
					erleichtern soll. Diese Struktur ist in den <a href="#info">Design Tokens</a> beschrieben.
				</p>

				<h2>Themes</h2>
				<p class="text-ui">
					Bisher sind zwei Theme implementiert: das Light-Theme und das Dark-Theme. Die Farben passen sich automatisch an das ausgewählte Theme an.
					Eine Anpassungen durch <code class="bg-ui-selected">dark:bg-ui</code> ist nicht mehr erforderlich.<br>
					Alle hier definierten Farben bieten eine Vorschau für beide Themes sowie die Kontrastwerte.
				</p>
				<h2>Technische Umsetzung</h2>
				<p class="text-ui">
					Farben sind in <code>/SVWS-Server/svws-webclient/ui/src/assets/styles/colors.css</code> definiert. Eine allgemeine Palette ist
					unter <code>/SVWS-Server/svws-webclient/ui/src/assets/styles/palette.css</code> definiert. Diese Farben sollen niemals direkt verwendet werden.
					Für die Definition der Farben wird auf Tailwind 4 und die Verwendung von Theme Variablen zurückgegriffen.
					<a href="https://tailwindcss.com/docs/theme" target="_blank" rel="noopener noreferrer">Theme variables</a>
				</p>
				<p class="text-ui">
					In <code>colors.css</code> werden die Farben für jeden Token angepasst. Dazu sollen ausschließlich Farben aus der UI Palette eingesetzt werden.
					<br>Mit Hilfe der Funktion <code>light-dark()</code> werden Farben sowohl für das Light- als auch für das Dark-Theme definiert.
				</p>
			</div>
		</Variant>
		<Variant title="Design Tokens" source=" " id="info">
			<div class="htw-prose dark:htw-prose-invert inline">
				<h1>Design Tokens</h1>
				<p class="text-ui">
					Um eine konsistente Farbgebung zu gewährleisten, verwenden wir ein Farbschema, das auf Design Tokens basiert. Die unten aufgeführten Tokens
					sind als CSS-Classes verfügbar und können in Komponenten und dem Client verwendet werden. Vereinzelte Kombinationen stehen jedoch nicht zur
					Verfügung. Valide Kombinationen können auf dieser Seite eingesehen werden.
				</p>
				<p class="text-ui">Die CSS-Klassen sind semantisch nach diesen Attributen zusammengesetzt:</p>
				<table class="text-left w-150 my-2">
					<thead class="border-b border-ui h-10">
						<tr>
							<th><em>[type]</em></th>
							<th>-ui</th>
							<th><em>-[role]</em></th>
							<th><em>-[prominence]</em></th>
							<th><em>-[interaction]</em></th>
						</tr>
					</thead>
					<tbody>
						<tr class="align-top">
							<td>
								<div v-for="(type, typeIndex) in semantics.type" :key="typeIndex">{{ type }}</div>
							</td>
							<td>
								<div class="opacity-50"><strong>-ui</strong></div>
								<div class="opacity-50"><strong>-uistatic</strong></div>
							</td>
							<td>
								<div><em>default</em></div>
								<div v-for="(role, roleIndex) in semantics.role" :key="roleIndex">{{ role }}</div>
							</td>
							<td>
								<div><em>default</em></div>
								<div v-for="(prominence, prominenceIndex) in semantics.prominence" :key="prominenceIndex">{{ prominence }}</div>
							</td>
							<td>
								<div><em>default</em></div>
								<div v-for="(interaction, interactionIndex) in semantics.interaction" :key="interactionIndex">{{ interaction }}</div>
							</td>
						</tr>
					</tbody>
				</table>
				<h2>Tokens</h2>
				<h3>Type</h3>
				<p class="text-ui">
					Der Type bezieht sich auf die Art, wie die Farbe angwendet werden soll. So steht <code class="bg-ui-selected">bg</code> zum Beispiel für backgroundcolor, während
					<code class="bg-ui-selected">text</code> für Textfarben steht.
				</p>
				<h3>ui</h3>
				<p class="text-ui">
					Der Zusatz <code class="bg-ui-selected">-ui</code> bzw. <code class="bg-ui-selected">-uistatic</code> markiert Farben aus dieser UI-Bibliothek.
					<code class="bg-ui-selected">-uistatic</code> steht dabei für statische Farben. Diese Farben verändern sich nicht basierend auf dem Theme, sondern
					sind für alle Themes gleich.
				</p>
				<h3>Role</h3>
				<p class="text-ui">
					Die Rolle beschreibt den Einsatz der Farbe. <code class="bg-ui-selected">success</code> zum Beispiel symbolisiert eine erfolgreiche Aktion und
					nutzt hierfür einen Grünton.
				</p>
				<p class="text-ui">
					Eine spezielle Funktion haben außerdem die <code class="bg-ui-selected">on</code>-Farben. Diese kommen zum Einsatz, wenn Farben über einer bestimmten
					Hintergrundfarbe liegen	wie zum Beispiel für Texte oder Borders. Wird eine Hintergrundfarbe definiert, die von der Defaultfarbe (bg-ui) abweicht,
					dann müssen die anderen	Farben entsprechend angepasst werden. Beispiel: bei der Hintergrundfarbe <code class="bg-ui-selected">bg-ui-success</code> ist für Texte die
					Farbe <code class="bg-ui-selected">text-ui-onsuccess</code> zu verwenden. Dieses Vorgehen stellt sicher, dass die Kontraste in allen Themes
					korrekt sind und bewahrt außerdem die Konsistenz. <br>
					Alle Variationen der <code class="bg-ui-selected">on</code>-Farben beziehen sich immer auf den Hintergrund. Das bedeutet eine Farbe wie
					<code class="bg-ui-selected">text-ui-onbrand-secondary</code> bezieht sich auf die Hintergrundfarbe
					<code class="bg-ui-selected">bg-ui-brand-secondary</code>. Das <code class="bg-ui-selected">text-ui-onbrand-secondary</code> bezieht sich
					hier nicht auf die Textfarbe in Kombination mit <code class="bg-ui-selected">bg-ui-brand</code>.
				</p>
				<h3>Prominence</h3>
				<p class="text-ui">
					Die Prominence der Farbe beschreibt ihre Hervorhebung. Die Werte <code class="bg-ui-selected">0</code> bis
					<code class="bg-ui-selected">100</code> bezeichnen Abstufungen von der Standardfarbe. <code class="bg-ui-selected">secondary</code> dagegen
					beschreibt eine Sekundärfarbe, die beispielsweise für Placeholder-Texte verwendet wird.
				</p>
				<h3>Interaction</h3>
				<p class="text-ui">
					Die Interaction beschreibt Farben, die bei einer bestimmten Interaktionen mit Elementen zum Einsatz kommen. die Interaction
					<code class="bg-ui-selected">hover</code> wird zum Beispiel verwendet, um die Farbe bei einem Hover zu verändern.
				</p>
				<h2>Konzept</h2>
				<p class="text-ui">
					Zur Erstellung des Farbschemas wurden Best Practices anderer Apps analysiert. Unter Berücksichtigung der Anforderungen aus dem SVWS-Client und
					der UI-Komponenten wurde das System mit Design Tokens definiert.
				</p>
				<p class="text-ui">
					Damit das Farbsystem flexibel und erweiterbar bleibt, wurden die Tokens semantisch nach Type, Role, Prominenz und Interaktion aufgeteilt. Damit
					ist es möglich das System für neue Komponenten und Features sinnvoll zu erweitern und sogar komplett neue Themes (z.B. High Contrast)
					unkompliziert zu ergänzen.
				</p>
				<ul>
					<li><a href="https://medium.muz.li/unlocking-the-power-of-design-tokens-to-create-dark-mode-ui-18c0802b094e" target="_blank" rel="noopener noreferrer">Unlocking the Power of Design Tokens to Create Dark Mode UI</a></li>
					<li><a href="https://tr.designtokens.org/format/#design-token" target="_blank" rel="noopener noreferrer">Design Tokens Format Module Draft Community Group Report</a></li>
					<li><a href="https://www.subframe.com/blog/how-to-setup-semantic-tailwind-colors" target="_blank" rel="noopener noreferrer">How to setup semantic Tailwind CSS colors</a></li>
					<li><a href="https://uxdesign.cc/naming-design-tokens-9454818ed7cb" target="_blank" rel="noopener noreferrer">Naming design tokens</a></li>
					<li><a href="https://www.figma.com/blog/illuminating-dark-mode/" target="_blank" rel="noopener noreferrer">The hidden challenges of a (seemingly) simple user request, and how we built a new pattern of designing and engineering at Figma.</a></li>
					<li><a href="https://www.figma.com/blog/the-future-of-design-systems-is-semantic/" target="_blank" rel="noopener noreferrer">The future of design systems is semantic</a></li>
					<li>Evolution of design tokens and component styling, <a href="https://medium.com/fast-design/evolution-of-design-tokens-and-component-styling-part-1-f1491ad1120e" target="_blank" rel="noopener noreferrer">part 1</a>, <a href="https://medium.com/fast-design/evolution-of-design-tokens-and-component-styling-part-2-1018e8bae62" target="_blank" rel="noopener noreferrer">part 2</a></li>
					<li><a href="https://bradfrost.com/blog/post/creating-themeable-design-systems/" target="_blank" rel="noopener noreferrer">creating themeable design systems</a></li>
					<li><a href="https://bradfrost.com/blog/post/the-many-faces-of-themeable-design-systems/" target="_blank" rel="noopener noreferrer">the many faces of themeable design systems</a></li>
					<li><a href="https://www.figma.com/community/file/1105905817981866740" target="_blank" rel="noopener noreferrer">How design tokens can make us better collaborators / Config 2022</a></li>
					<li><a href="https://medium.com/thinking-design/adaptive-color-in-design-systems-7bcd2e664fa0" target="_blank" rel="noopener noreferrer">Adaptive Color in Design Systems</a></li>
				</ul>
			</div>
		</Variant>
		<Variant title="Verwendung" source=" " id="verwendung">
			<div class="htw-prose dark:htw-prose-invert inline">
				<h1>Verwendung der Farben</h1>
				<p class="text-ui">
					SVWS-UI bietet eine breite Palette von aufeinander abgestimmten Farben, die für die verschiedensten Anwendungsfälle zum Einsatz kommen können.
					Durch die richtige Verwendung dieser Farben kann eine konsistente Benuzteroberfläche geschaffen werden, die zudem auch den Richtlinien der
					Barrierefreiheit in Bezug auf Kontraste entspricht.
				</p>
				<p class="text-ui">
					Wichtig für das Verständnis der Farben ist zu wissen, dass die Farben keine direkte Funktion haben. Ihre Namen demosntrieren nur, wofür sie
					verwendet werden sollen. Ein Beispiel: Die Farbe <code class="bg-ui-selected">text-ui-hover</code> ist eine Textfarbe, die verwendet werden soll,
					sobald der Text gehovert wird. Das setzen der Klasse alleine sorgt aber nicht für den Effekt.<br>
					<span><strong class="text-ui-danger">Der folgende Code erzielt NICHT den gewünschten Hovereffekt</strong></span>
				</p>
				<h3>Code</h3>
				<pre class="w-fit">&lt;div class=&quot;bg-ui text-ui text-ui-hover&quot;&gt;Hover me!&lt;/div&gt;</pre>
				<h3>Ergebnis</h3>
				<div class="bg-ui text-ui text-ui-hover">Hover me!</div>
				<p class="text-ui">
					Wird eine Textfarbe so definiert, dann wird der Text immer die Farbe <code class="bg-ui-selected">text-ui-hover</code> haben, egal ob er gehovert
					wird oder nicht. <br>
					<span><strong class="text-ui-success">Der folgende Code erzielt den gewünschten Hovereffekt</strong></span>
				</p>
				<h3>Code</h3>
				<pre class="w-fit">&lt;div class=&quot;bg-ui text-ui hover:text-ui-hover&quot;&gt;Hover me!&lt;/div&gt;</pre>
				<h3>Ergebnis</h3>
				<div class="bg-ui text-ui hover:text-ui-hover">Hover me!</div>
				<h2>Regeln</h2>
				<p class="text-ui">
					Um die Ziele der Barrierefreiheit und der konsistenten Benutzeroberfläche zu erreichen, müssen einige Regeln beachtet werden:
				</p>
				<ol>
					<li>
						<strong>Ausschließliche Nutzung der hier aufgeführten Farben:</strong>Es müssen ausschließlich die hier aufgeführten Farben verwendet werden.
						Die alten CSS-Color-Classes (Tailwind defaults) dürfen nicht mehr verwendet werden. Verwendbare SVWS-Farben sind an dem Zusatz
						<code class="bg-ui-selected">-ui</code> bzw. <code class="bg-ui-selected">-uistatic</code> zu erkennen.
					</li>
					<li>
						<strong>Kontraste beachten:</strong> Für eine barrierefreie Webseite müssen kontrastreiche Farben gewählt werden. Diese müssen den Standards der <a href="https://www.w3.org/WAI/WCAG21/Understanding/contrast-minimum.html" target="_blank" class="text-ui-brand">Web Content Accessibility Guidelines (WCAG) 2.1</a>
						entsprechen und mindestens das Level AA erreichen, besser noch AAA. Die entsprechenden Kontraste und Level sind auch hier bei allen Farben
						angegeben. <br>
						Bei Hintergrundfarben kann ein ausreichender Kontrast auch mit der Hilfe von Borders erreicht werden.
					</li>
					<li>
						<strong>Passende Farben auf Hintergrundfarben:</strong> Für die meisten Hintergrundfarben gibt es abgestimmte Farben für Texte, Icons, Borders etc.
						Die Namen der Farben orientieren sich dann an dem der Hintergrundfarbe, nur mit einem <code class="bg-ui-selected">on</code> Zusatz bei der Rolle. <br>
						Beispiel: Für die Hintergrundfarbe <code class="bg-ui-selected">bg-ui-success</code> sind die Textfarben <code class="bg-ui-selected">text-ui-onsuccess</code>,
						<code class="bg-ui-selected">text-ui-onsuccess-hover</code> oder <code class="bg-ui-selected">text-ui-onsuccess-secondary</code> zu verwenden. Die richtige Nutzung der Farben sorgt für eine abgestimmte,
						kontrastreiche Kombination, die sich durch die gesamte Benutzeroberfläche zieht.
					</li>
					<li>
						<strong>Keine speziellen Theme-Anpassungen:</strong> Abhängig vom aktuell angezeigten Theme werden auch automatisch passende Farben verwendet.
						So ist <code class="bg-ui-selected">bg-ui</code> im Light-Theme hell und im Dark-Theme dunkel, ohne das selbst Anpassungen vorgenommen werden
						müssen. Klassendefinitionen wie <code class="bg-ui-selected">dark:bg-ui</code> sind daher nicht mehr notwendig. <br>
						Sollte es aber dennoch nötig sein, dass Farben in einem anderen Theme angezeigt werden, obwohl dieses gerade nicht verwendet wird, reicht
						es aus, dem enstsprechenden Element die Klasse zu geben, zum Beispiel:
						<code class="bg-ui-selected">&lt;div class=&quot;dark bg-ui text-ui&quot;&gt;Text&lt;/div&gt;</code>.
						Es wird dann für den Hintergrund und die Textfarbe automatisch die Farbe aus dem Dark-Theme gewählt.
					</li>
					<li>
						<strong>Verwendung von statischen Farben:</strong> Spezielle Farben mit dem Zusatz <code class="bg-ui-selected">-uistatic</code> sind
						statische Farben, die unabhängig vom Theme immer gleich aussehen. Verwende sie daher immer nur explizit dann, wenn dieser Effekt gewollt
						ist. Das ist zum Beispiel im SVWS-Client der Fall, wenn Fächerfarben vom Server geladen werden.
					</li>
				</ol>
				<h2>Erweiterungen</h2>
				<p class="text-ui">
					Bei Bedarf kann das System durch weitere Farben bzw. Farbabstufungen und -kombinationen erweitert werden. In den meisten Fällen sollte für jede
					Anforderung jedoch eine passende Kombination aus den vorhandenen Tokens gefunden werden können und es müssen die Richtlinien für Kontraste
					nach WCAG berücksichtigt werden.
				</p>
				<h2>Barrierefreiheit nach WCAG 2</h2>
				<p class="text-ui">
					Grundsätzlich muss mindestens ein Kontrastlevel von AA nach WCAG erreicht werden. Besser ist ein Kontrast von AAA. Welche Kontraste die Farben
					bezogen auf den Hintergrund erreichen, kann auf den jeweiligen Farbseiten geprüft werden. Farben, die ein Level von AA erreichen, werden gelb
					markiert. Farben, die ein Level von AAA erreichen, werden grün markiert. Farben, die kein Level erreichen, werden rot markiert.
				</p>
				<p class="text-ui">
					In Zukunft kann ein weiteres Farbschema neben Light und Dark Mode ergänzt werden, welches AAA priorisiert.
				</p>
				<p class="text-ui">
					Weiterführende Links:
				</p>
				<ul>
					<li><a href="https://www.w3.org/WAI/WCAG21/Understanding/conformance#levels" target="_blank" rel="noopener noreferrer">Understanding Levels of Conformance (AA, AAA)</a></li>
					<li><a href="https://www.w3.org/WAI/WCAG21/Understanding/contrast-minimum.html" target="_blank" rel="noopener noreferrer">Contrast (Minimum) (Level AA)</a></li>
					<li><a href="https://www.w3.org/WAI/WCAG21/Understanding/contrast-enhanced.html" target="_blank" rel="noopener noreferrer">Contrast (Enhanced) (Level AAA)</a></li>
					<li><a href="https://www.w3.org/WAI/WCAG22/Understanding/non-text-contrast.html" target="_blank" rel="noopener noreferrer">Non-text Contrast</a></li>
					<li><a href="https://www.w3.org/TR/WCAG21/#dfn-contrast-ratio" target="_blank" rel="noopener noreferrer">Contrast Ratio</a></li>
					<li><a href="https://www.w3.org/TR/WCAG21/#dfn-relative-luminance" target="_blank" rel="noopener noreferrer">Relative Luminance</a></li>
				</ul>
				<p class="text-ui">Mit verschiedenen Tools können Farben auch einzeln getestet werden:</p>
				<ul>
					<li><a href="https://color.adobe.com/create/color-contrast-analyzer" target="_blank" rel="noopener noreferrer">Adobe Color Accessibility Tools: Contrast Checker</a></li>
					<li><a href="https://medium.com/@NateBaldwin/leonardo-an-open-source-contrast-based-color-generator-92d61b6521d2" target="_blank" rel="noopener noreferrer">Leonardo: an open source contrast-based color generator</a></li>
					<li><a href="https://leonardocolor.io/tools.html#" target="_blank" rel="noopener noreferrer">Leonardo Color Toolbox (Contrast Check)</a></li>
					<li><a href="https://leonardocolor.io/theme.html#" target="_blank" rel="noopener noreferrer">Leonardo Adaptive color theme tool</a></li>
				</ul>
			</div>
		</Variant>
		<Variant :title="`${type}-ui`" source=" " :id="`tokens-${type}`" v-for="([type, typeMap]) in typeList" :key="type">
			<div v-if="type === 'bg'" class="htw-prose dark:htw-prose-invert inline">
				<h1>Hintergrundfarben</h1>
				<p class="text-ui">
					Folgende Farben werden für Hintergründe verwendet. Es ist zu beachten, dass die Kontrastrichtlinien von WCAG eingehalten werden. Um
					diese zu erreichen, können zusätzliche Borders verwendet werden.
				</p>
			</div>
			<div v-if="type === 'text'" class="htw-prose dark:htw-prose-invert inline">
				<h1>Textfarben</h1>
				<p class="text-ui">
					Folgende Farben werden für Texte verwendet. Es ist zu beachten, dass die Kontrastrichtlinien von WCAG eingehalten werden. Um
					diese zu erreichen, müssen die <code class="bg-ui-selected">-on</code> Farben zu den passenden Hintergründen verwendet werden. Beispiel:
					Für die Hintergrundfarbe <code class="bg-ui-selected">bg-ui-success</code> ist die Textfarbe
					<code class="bg-ui-selected">text-ui-onsuccess</code> zu verwenden. Für <code class="bg-ui-selected">bg-ui-success-hover</code> hingegen ist
					die Textfarbe <code class="bg-ui-selected">text-ui-onsuccess-hover</code> zu verwenden. <br>
					Das erriechte WCAG Kontrastlevel ist außerdem abhängig von der Schriftgröße. In der Vorschau wird die für SVWS normale Schriftgröße (9pt)
					verwendet und das enstprechende Kontrastlevel angegeben. Für die Vorschau von weiteren Schriftgrößen, klicke auf eine Farbkachel.
				</p>
			</div>
			<div v-if="type === 'border'" class="htw-prose dark:htw-prose-invert inline">
				<h1>Borderfarben</h1>
				<p class="text-ui">
					Folgende Farben werden für Borders (Rahmen) verwendet. Es ist zu beachten, dass die Kontrastrichtlinien von WCAG eingehalten werden. Um
					diese zu erreichen, müssen die <code class="bg-ui-selected">-on</code> Farben zu den passenden Hintergründen verwendet werden. Beispiel:
					Für die Hintergrundfarbe <code class="bg-ui-selected">bg-ui-success</code> ist die Borderfarbe
					<code class="bg-ui-selected">border-ui-onsuccess</code> zu verwenden. Für <code class="bg-ui-selected">bg-ui-success-hover</code> hingegen ist
					die Borderfarbe <code class="bg-ui-selected">border-ui-onsuccess-hover</code> zu verwenden. <br>
				</p>
			</div>
			<div v-if="type === 'accent'" class="htw-prose dark:htw-prose-invert inline">
				<h1>Accentfarben</h1>
				<p class="text-ui">
					Folgende Farben werden für Akzente verwendet. Sie dienen zum Einfärben von Checkboxen, Radiobuttons, Rangs-Slidern etc.
					Es ist zu beachten, dass die Kontrastrichtlinien von WCAG eingehalten werden. Um diese zu erreichen, müssen die
					<code class="bg-ui-selected">-on</code> Farben zu den passenden Hintergründen verwendet werden. Beispiel:
					Für die Hintergrundfarbe <code class="bg-ui-selected">bg-ui-success</code> ist die Accentfarben
					<code class="bg-ui-selected">accent-ui-onsuccess</code> zu verwenden.
				</p>
			</div>
			<div v-if="type === 'ring'" class="htw-prose dark:htw-prose-invert inline">
				<h1>Ringfarben</h1>
				<p class="text-ui">
					Folgende Farben werden für Ringe verwendet. Sie dienen zur visuellen Hervorhebung eines Elements zum Beispiel bei einem Fokus oder Hover.
					Ringfarben umschließen das Element und sind daher nicht mit den Borderfarben zu verwechseln.
				</p>
			</div>
			<div v-if="type === 'icon'" class="htw-prose dark:htw-prose-invert inline">
				<h1>Iconfarben</h1>
				<p class="text-ui">
					Folgende Farben werden für Icons verwendet. Es ist zu beachten, dass die Kontrastrichtlinien von WCAG eingehalten werden. Um
					diese zu erreichen, müssen die <code class="bg-ui-selected">-on</code> Farben zu den passenden Hintergründen verwendet werden. Beispiel:
					Für die Hintergrundfarbe <code class="bg-ui-selected">bg-ui-success</code> ist die Iconfarbe
					<code class="bg-ui-selected">icon-ui-onsuccess</code> zu verwenden. Für <code class="bg-ui-selected">bg-ui-success-hover</code> hingegen ist
					die Iconfarbe <code class="bg-ui-selected">icon-ui-onsuccess-hover</code> zu verwenden. <br>
				</p>
			</div>
			<div v-if="type === 'shadow'" class="htw-prose dark:htw-prose-invert inline">
				<h1>Shadowfarben</h1>
				<p class="text-ui">
					Folgende Farben werden für Schattierungen verwendet. Wenn diese rein zum Styling verwendet werden, ohne inhaltlichen Wert zu übermitteln,
					müssen sie keinen WCAG Kontrastrichtlienien entsprechen.
				</p>
			</div>
			<div :class="[(type === 'bg') ? 'grid-cols-[1fr_auto_auto_auto_auto]' : 'grid-cols-[1fr_auto_auto_auto_auto_auto_auto]', 'grid rounded-md m-2']"
				class="mt-10">
				<div class="contents font-bold text-center border-b border-ui py-2">
					<div>CSS-Klasse</div>
					<div :class="[(type === 'bg') ? 'col-span-2' : 'col-span-3']">Light Theme</div>
					<div :class="[(type === 'bg') ? 'col-span-2' : 'col-span-3']">Dark Theme</div>
				</div>
				<div class="border-b border-ui" />
				<div class="grid grid-cols-subgrid col-start-2 font-medium text-ui-secondary border-b border-ui py-2 text-center"
					:class="[(type === 'bg') ? 'col-span-4 [&>*]:w-20' : 'col-span-6 [&>*]:w-18']">
					<div>Farbe</div>
					<div v-if="type !== 'bg'">Vorschau</div>
					<div>Kontrast</div>
					<div>Farbe</div>
					<div v-if="type !== 'bg'">Vorschau</div>
					<div>Kontrast</div>
				</div>

				<div v-for="([role, roleColors]) in typeMap" :key="role" class="relative grid grid-cols-subgrid col-span-full my-2">
					<div class="border-t border-x border-ui rounded-t-lg bg-ui-brand-secondary text-xs font-semibold col-span-full w-fit p-2">{{ role }}</div>
					<div class="grid grid-cols-subgrid col-span-full border border-ui rounded-lg rounded-tl-none overflow-hidden">
						<div v-for="color in roleColors" :key="color" class="contents swatch-colors">
							<!-- Vollständiger Farbname -->
							<div class="font-mono flex items-center p-2">
								<svws-ui-tooltip position="top">
									<svws-ui-button type="icon" @click="copyToClipboard(color)" class="mr-2">
										<span class="icon i-ri-file-copy-line" @click="copyToClipboard(color)" />
									</svws-ui-button>
									<template #content>
										Kopieren
									</template>
								</svws-ui-tooltip>

								<span v-for="(part, partIndex) in color.split('-')" :key="partIndex" class="font-mono"
									:class="{ 'font-bold': partIndex < 2, 'opacity-50': part === 'ui' || part === 'uistatic',
										'text-ui-selected font-bold': role !== 'default' && partIndex === 2 }">
									{{ part }}<span v-if="partIndex < color.split('-').length - 1">-</span>
								</span>
							</div>
							<div v-for="(isDark, index) in [false, true]" :key="index" :class="isDark ? 'dark' : 'light'" class="contents">
								<Theme :contrast-values="getContrast(color, isDark)" :background="getBackgroundColor(color)">
									<template #default="{ contrast, backgroundColor }">
										<div :id="`${color}${isDark === true ? '-dark' : ''}`" class="p-2 flex items-center justify-center relative"
											:class="backgroundColor">
											<div class="w-12 h-8 rounded-lg z-0" :style="{ backgroundColor: `var(--color-${color})` }" />
											<div v-if="type === 'text'" class="w-12 h-8 rounded-lg absolute z-10 cursor-pointer bg-ui-selected opacity-0
												hover:opacity-100 hover:ring-2 hover:ring-ui-brand flex items-center justify-center"
												@click="setColorPreviewInformation(color, isDark)">
												<span class="i-ri-eye-line icon-lg inline-block" />
											</div>
										</div>
										<div v-if="type !== 'bg'" :class="backgroundColor" class="flex flex-col justify-center items-center">
											<div>
												<span v-if="type === 'icon'" class="i-ri-archive-line icon-xl inline-block" :class="color" />
												<div v-if="type === 'text'">
													<div>
														<div :class="color" class="text-[9pt]">Text</div>
														<div :class="color" class="text-[9pt] font-bold">Text</div>
													</div>
												</div>
												<div v-if="type === 'border'" class="w-12 h-8 rounded-lg border-2" :style="{ borderColor: `var(--color-${color})` }" />
												<div v-if="type=== 'accent'" :class="color">
													<input type="checkbox" checked style="pointer-events: none; accent-color: inherit;">
												</div>
												<div v-if="type === 'ring'" class="w-12 h-8 rounded-lg ring-2" :style="{ color: `var(--color-${color})` }" />
												<div v-if="type === 'shadow'">
													<div class="w-12 h-8 rounded-lg bg-ui border border-ui" :style="{ boxShadow: `0 4px 6px -1px var(--color-${color})` }" />
												</div>
											</div>
										</div>
										<div class="flex font-bold items-center justify-center" :class="[backgroundColor, getContrastColor(contrast.contrastLevel)]">
											<div class="bg-ui px-1 rounded-full w-13 text-center">{{ contrast.contrastRatio }}</div>
										</div>
									</template>
								</Theme>
							</div>
						</div>
					</div>
				</div>
			</div>

			<template #controls>
				<div class="p-2">
					<h2 class="text-lg font-semibold text-ui">WCAG Kontrastverhältnisse</h2>
					<p class="py-2">
						Um die Barrierefreiheitsrichtlinien von WCAG zu erfüllen, muss mindestens ein Kontrastlevel von <span class="text-bold">AA</span> angestrebt werden. <br>
						Weitere Informationen:
						<a href="https://www.w3.org/WAI/WCAG21/Understanding/contrast-minimum.html" target="_blank" class="text-ui-brand">Web Content Accessibility Guidelines (WCAG) 2.1</a>
					</p>
					<div class="grid grid-flow-col grid-rows-4 [&>*]:bg-ui text-center border border-ui-neutral rounded-lg mb-2 overflow-hidden">
						<div class="text-ui-secondary font-medium py-2 border-b border-ui-neutral">Kontrast Light</div>
						<div class="text-ui-danger font-bold py-2">4.49:1</div>
						<div class="text-ui-warning font-bold py-2">4.50:1</div>
						<div class="text-ui-success font-bold py-2">7.00:1</div>
						<div class="text-ui-secondary font-medium py-2 border-b border-ui-neutral">Kontrast Dark</div>
						<div class="dark text-ui-danger font-bold py-2">4.49:1</div>
						<div class="dark text-ui-warning font-bold py-2">4.50:1</div>
						<div class="dark text-ui-success font-bold py-2">7.00:1</div>
						<div class="text-ui-secondary font-medium py-2 border-b border-ui-neutral">Kontrastlevel</div>
						<div class="text-ui py-2">&lt;AA</div>
						<div class="text-ui py-2">AA</div>
						<div class="text-ui py-2">AAA</div>
					</div>
					<div v-if="type === 'text'">
						<h2 class="text-lg font-semibold text-ui mt-2">Kontraste für Schriftgrößen</h2>
						<div class="py-2">Kontrast: <span class="font-bold">{{ colorPreview.contrast }}</span></div>
						<div v-if="colorPreview.color !== ''">
							<div class="text-ui grid grid-cols-[1fr_auto_auto] gap-x-2 items-center m-2 [&>div]:p-2 [&>div]:h-full">
								<!-- Header -->
								<div class="text-headline-sm">Vorschau</div>
								<div class="text-center text-headline-sm">Schriftgröße</div>
								<div class="text-center text-headline-sm">Level</div>
								<hr class="col-span-full">
								<!-- Zeile 1 -->
								<div class="text-[9pt]" :class="[{ 'dark': colorPreview.dark }, colorPreview.background, colorPreview.color]">Normaler Text</div>
								<div class="text-center">9pt</div>
								<div class="text-center">{{ colorPreview.contrastLevels[0] }}</div>
								<!-- Zeile 2 -->
								<div class="text-[9pt] font-bold" :class="[{ 'dark': colorPreview.dark }, colorPreview.background, colorPreview.color]">Fetter normaler Text</div>
								<div class="text-center">9pt</div>
								<div class="text-center">{{ colorPreview.contrastLevels[1] }}</div>
								<!-- Zeile 3 -->
								<div class="text-[9pt]" :class="[{ 'dark': colorPreview.dark }, colorPreview.background, colorPreview.color]">WCAG Text</div>
								<div class="text-center">14pt</div>
								<div class="text-center">{{ colorPreview.contrastLevels[2] }}</div>
								<!-- Zeile 4 -->
								<div class="text-[14pt] font-bold" :class="[{ 'dark': colorPreview.dark }, colorPreview.background, colorPreview.color]">Fetter WCAG Text</div>
								<div class="text-center">14pt</div>
								<div class="text-center">{{ colorPreview.contrastLevels[3] }}</div>
								<!-- Zeile 5 -->
								<div class="text-[14pt]" :class="[{ 'dark': colorPreview.dark }, colorPreview.background, colorPreview.color]">Großer Text</div>
								<div class="text-center">18pt</div>
								<div class="text-center">{{ colorPreview.contrastLevels[4] }}</div>
								<!-- Zeile 6 -->
								<div class="text-[14pt] font-bold" :class="[{ 'dark': colorPreview.dark }, colorPreview.background, colorPreview.color]">Fetter Großer Text</div>
								<div class="text-center">18pt</div>
								<div class="text-center">{{ colorPreview.contrastLevels[5] }}</div>
							</div>
						</div>
						<div v-else>
							Für die Vorschau von Farben in verschiedenen Schriftgrößen, klicke auf eine Farbkachel.
						</div>
					</div>
				</div>
			</template>
		</Variant>
	</Story>
</template>

<script setup lang="ts">

	import type { PropType} from 'vue';
	import { defineComponent, reactive } from 'vue';
	import { DeveloperNotificationException } from '../../../core/src';


	// Folgende Maps enthalten alle definierten Farben
	const textColorMap: Map<string, string[]> = new Map([
		['default', [
			'text-ui', 'text-ui-secondary', 'text-ui-0', 'text-ui-10', 'text-ui-25',
			'text-ui-50', 'text-ui-75', 'text-ui-100', 'text-ui-hover',
		]],
		['brand', [
			'text-ui-brand', 'text-ui-brand-hover',
			'text-ui-brand-secondary', 'text-ui-brand-secondary-hover',
		]],
		['statistic', [
			'text-ui-statistic', 'text-ui-statistic-hover',
			'text-ui-statistic-secondary', 'text-ui-statistic-secondary-hover',
		]],
		['selected', [
			'text-ui-selected', 'text-ui-selected-hover',
			'text-ui-selected-secondary', 'text-ui-selected-secondary-hover',
		]],
		['danger', [
			'text-ui-danger', 'text-ui-danger-hover',
			'text-ui-danger-secondary', 'text-ui-danger-secondary-hover',
		]],
		['success', [
			'text-ui-success', 'text-ui-success-hover',
			'text-ui-success-secondary', 'text-ui-success-secondary-hover',
		]],
		['warning', [
			'text-ui-warning', 'text-ui-warning-hover',
			'text-ui-warning-secondary', 'text-ui-warning-secondary-hover',
		]],
		['caution', [
			'text-ui-caution', 'text-ui-caution-hover', 'text-ui-caution-secondary',
		]],
		['neutral', [
			'text-ui-neutral', 'text-ui-neutral-hover', 'text-ui-neutral-secondary',
		]],
		['disabled', [
			'text-ui-disabled', 'text-ui-disabled-secondary',
		]],
		['onbrand', [
			'text-ui-onbrand', 'text-ui-onbrand-hover', 'text-ui-onbrand-secondary',
		]],
		['onstatistic', [
			'text-ui-onstatistic', 'text-ui-onstatistic-hover', 'text-ui-onstatistic-secondary',
		]],
		['onselected', [
			'text-ui-onselected', 'text-ui-onselected-hover', 'text-ui-onselected-secondary',
		]],
		['ondanger', [
			'text-ui-ondanger', 'text-ui-ondanger-hover', 'text-ui-ondanger-secondary',
		]],
		['onsuccess', [
			'text-ui-onsuccess', 'text-ui-onsuccess-hover','text-ui-onsuccess-secondary',
		]],
		['onwarning', [
			'text-ui-onwarning', 'text-ui-onwarning-hover',	'text-ui-onwarning-secondary',
		]],
		['oncaution', [
			'text-ui-oncaution', 'text-ui-oncaution-hover',	'text-ui-oncaution-secondary',
		]],
		['onneutral', [
			'text-ui-onneutral', 'text-ui-onneutral-hover', 'text-ui-onneutral-secondary',
		]],
		['ondisabled', [
			'text-ui-ondisabled', 'text-ui-ondisabled-secondary',
		]],
		['uistatic', [
			'text-uistatic', 'text-uistatic-0', 'text-uistatic-10', 'text-uistatic-25', 'text-uistatic-50', 'text-uistatic-75', 'text-uistatic-100',
		]],
	]);

	const borderColorMap: Map<string, string[]> = new Map([
		['default', ['border-ui', 'border-ui-0', 'border-ui-10', 'border-ui-25', 'border-ui-50', 'border-ui-75', 'border-ui-100', 'border-ui-hover']],
		['brand', ['border-ui-brand', 'border-ui-brand-hover']],
		['statistic', ['border-ui-statistic', 'border-ui-statistic-hover']],
		['selected', ['border-ui-selected', 'border-ui-selected-hover']],
		['danger', ['border-ui-danger', 'border-ui-danger-hover']],
		['success', ['border-ui-success', 'border-ui-success-hover']],
		['warning', ['border-ui-warning', 'border-ui-warning-hover']],
		['caution', ['border-ui-caution', 'border-ui-caution-hover']],
		['neutral', ['border-ui-neutral', 'border-ui-neutral-hover']],
		['disabled', ['border-ui-disabled']],
		['onbrand', ['border-ui-onbrand', 'border-ui-onbrand-hover']],
		['onstatistic', ['border-ui-onstatistic', 'border-ui-onstatistic-hover']],
		['onselected', ['border-ui-onselected', 'border-ui-onselected-hover']],
		['ondanger', ['border-ui-ondanger', 'border-ui-ondanger-hover']],
		['onsuccess', ['border-ui-onsuccess', 'border-ui-onsuccess-hover']],
		['onwarning', ['border-ui-onwarning', 'border-ui-onwarning-hover']],
		['oncaution', ['border-ui-oncaution', 'border-ui-oncaution-hover']],
		['onneutral', ['border-ui-onneutral', 'border-ui-onneutral-hover']],
		['ondisabled', ['border-ui-ondisabled']],
		['uistatic', ['border-uistatic', 'border-uistatic-0', 'border-uistatic-10', 'border-uistatic-25', 'border-uistatic-50', 'border-uistatic-75', 'border-uistatic-100']],
	]);


	const backgroundColorMap: Map<string, string[]> = new Map([
		['default', ['bg-ui', 'bg-ui-0', 'bg-ui-10', 'bg-ui-25', 'bg-ui-50', 'bg-ui-75', 'bg-ui-100', 'bg-ui-hover']],
		['brand', ['bg-ui-brand', 'bg-ui-brand-hover', 'bg-ui-brand-secondary']],
		['statistic', ['bg-ui-statistic', 'bg-ui-statistic-hover', 'bg-ui-statistic-secondary']],
		['selected', ['bg-ui-selected', 'bg-ui-selected-hover']],
		['danger', ['bg-ui-danger', 'bg-ui-danger-hover', 'bg-ui-danger-secondary']],
		['success', ['bg-ui-success', 'bg-ui-success-hover', 'bg-ui-success-secondary']],
		['warning', ['bg-ui-warning', 'bg-ui-warning-hover', 'bg-ui-warning-secondary']],
		['caution', ['bg-ui-caution', 'bg-ui-caution-hover', 'bg-ui-caution-secondary']],
		['neutral', ['bg-ui-neutral', 'bg-ui-neutral-hover', 'bg-ui-neutral-secondary']],
		['disabled', ['bg-ui-disabled']],
		['uistatic', ['bg-uistatic', 'bg-uistatic-0', 'bg-uistatic-10', 'bg-uistatic-25', 'bg-uistatic-50', 'bg-uistatic-75', 'bg-uistatic-100']],
	]);


	const accentColorMap: Map<string, string[]> = new Map([
		['default', ['accent-ui']],
		['brand', ['accent-ui-brand']],
		['statistic', ['accent-ui-statistic']],
		['selected', ['accent-ui-selected']],
		['danger', ['accent-ui-danger']],
		['success', ['accent-ui-success']],
		['warning', ['accent-ui-warning']],
		['caution', ['accent-ui-caution']],
		['neutral', ['accent-ui-neutral']],
		['disabled', ['accent-ui-disabled']],
		['onbrand', ['accent-ui-onbrand']],
		['onstatistic', ['accent-ui-onstatistic']],
		['onselected', ['accent-ui-onselected']],
		['ondanger', ['accent-ui-ondanger']],
		['onsuccess', ['accent-ui-onsuccess']],
		['onwarning', ['accent-ui-onwarning']],
		['oncaution', ['accent-ui-oncaution']],
		['onneutral', ['accent-ui-onneutral']],
		['ondisabled', ['accent-ui-ondisabled']],
	]);

	const ringColorMap: Map<string, string[]> = new Map([
		['default', ['ring-ui']],
		['brand', ['ring-ui-brand']],
		['statistic', ['ring-ui-statistic']],
		['danger', ['ring-ui-danger']],
		['success', ['ring-ui-success']],
		['warning', ['ring-ui-warning']],
		['caution', ['ring-ui-caution']],
		['neutral', ['ring-ui-neutral']],
	]);

	const iconColorMap: Map<string, string[]> = new Map([
		['default', ['icon-ui', 'icon-ui-0', 'icon-ui-10', 'icon-ui-25', 'icon-ui-50', 'icon-ui-75', 'icon-ui-100', 'icon-ui-hover', 'icon-ui-secondary',
			'icon-ui-secondary-hover']],
		['brand', ['icon-ui-brand', 'icon-ui-brand-hover', 'icon-ui-brand-secondary', 'icon-ui-brand-secondary-hover']],
		['statistic', ['icon-ui-statistic', 'icon-ui-statistic-hover', 'icon-ui-statistic-secondary', 'icon-ui-statistic-secondary-hover']],
		['selected', ['icon-ui-selected', 'icon-ui-selected-hover', 'icon-ui-selected-secondary', 'icon-ui-selected-secondary-hover']],
		['danger', ['icon-ui-danger', 'icon-ui-danger-hover', 'icon-ui-danger-secondary', 'icon-ui-danger-secondary-hover']],
		['success', ['icon-ui-success', 'icon-ui-success-hover', 'icon-ui-success-secondary', 'icon-ui-success-secondary-hover']],
		['warning', ['icon-ui-warning', 'icon-ui-warning-hover', 'icon-ui-warning-secondary', 'icon-ui-warning-secondary-hover']],
		['caution', ['icon-ui-caution', 'icon-ui-caution-hover', 'icon-ui-caution-secondary', 'icon-ui-caution-secondary-hover']],
		['neutral', ['icon-ui-neutral', 'icon-ui-neutral-hover', 'icon-ui-neutral-secondary', 'icon-ui-neutral-secondary-hover']],
		['disabled', ['icon-ui-disabled', 'icon-ui-disabled-secondary']],
		['onbrand', ['icon-ui-onbrand', 'icon-ui-onbrand-hover', 'icon-ui-onbrand-secondary']],
		['onstatistic', ['icon-ui-onstatistic', 'icon-ui-onstatistic-hover', 'icon-ui-onstatistic-secondary']],
		['onselected', ['icon-ui-onselected', 'icon-ui-onselected-hover', 'icon-ui-onselected-secondary']],
		['ondanger', ['icon-ui-ondanger', 'icon-ui-ondanger-hover', 'icon-ui-ondanger-secondary']],
		['onsuccess', ['icon-ui-onsuccess', 'icon-ui-onsuccess-hover', 'icon-ui-onsuccess-secondary']],
		['onwarning', ['icon-ui-onwarning', 'icon-ui-onwarning-hover', 'icon-ui-onwarning-secondary']],
		['oncaution', ['icon-ui-oncaution', 'icon-ui-oncaution-hover', 'icon-ui-oncaution-secondary']],
		['onneutral', ['icon-ui-onneutral', 'icon-ui-onneutral-hover', 'icon-ui-onneutral-secondary']],
		['ondisabled', ['icon-ui-ondisabled', 'icon-ui-ondisabled-secondary']],
		['uistatic', ['icon-uistatic', 'icon-uistatic-0', 'icon-uistatic-10', 'icon-uistatic-25', 'icon-uistatic-50', 'icon-uistatic-75', 'icon-uistatic-100']],
	]);

	const shadowColorMap: Map<string, string[]> = new Map([
		['default', ['shadow-ui-0', 'shadow-ui-10', 'shadow-ui-25', 'shadow-ui-50', 'shadow-ui-75', 'shadow-ui-100'	]],
	]);

	// Eine Map, die den type-Namen zusammen mit den dazugehörigen Farben enthält.
	const typeList = new Map([["bg", backgroundColorMap], ["text", textColorMap], ['border', borderColorMap], ['accent', accentColorMap],
		['ring', ringColorMap], ['icon', iconColorMap], ['shadow', shadowColorMap]]);


	// Zusammensetzung alle Farben
	const semantics = {
		type: ['bg', 'text', 'border', 'accent', 'ring', 'icon'],
		role: ['', 'brand', 'statistic', 'selected', 'danger', 'success', 'warning', 'caution', 'neutral', 'disabled', 'onbrand', 'onstatistic', 'onselected', 'ondanger', 'onsuccess', 'onwarning', 'oncaution', 'onneutral', 'ondisabled'],
		prominence: ['', 'secondary', '0', '10', '25', '50', '75', '100'],
		interaction: ['', 'hover'],
	}

	// Eine Inline-Komponente, die pro Theme verwendet wird. Das Uaslagern in diese Komponente verhindert, dass im Template mehrmals Funktionen für Kontraste
	// und Hintergrundfarben aufgerufen werden müssen.
	const Theme = defineComponent({
		props: {
			contrastValues: {
				type: Object as PropType<{ contrastRatio: string; contrastLevel: string }>,
				required: true,
			},
			background: {
				type: String,
				required: true,
			},
		},
		setup(props, { slots }) {
			return () => {
				return slots.default ? slots.default({ contrast: props.contrastValues, backgroundColor: props.background }) : null
			}
		},
	});

	// Enthält alle Informationen für die Vorschau der Textfarben auf der rechten Bildschirmseite.
	const colorPreview = reactive ({
		color: '',
		background: '',
		dark: false,
		contrast: 'N/A',
		contrastLevels: [] as string[],
	});

	/**
	 * Setzt die Farben und Kontrastwerte in der Farbvorschau für Texte auf der rechten Seite.
	 *
	 * @param color   die Farbe, die für die Preview gesetzt werden soll
	 * @param dark    bestimmt, ob die Farbe im light oder dark-Mode angezeigt werden soll
	 */
	function setColorPreviewInformation(color: string, dark: boolean) {
		colorPreview.color = color;
		colorPreview.background = getBackgroundColor(color);
		colorPreview.dark = dark;
		const { contrastRatio: contrast, contrastLevel: score} = getContrast(color, dark);
		colorPreview.contrast = contrast;
		colorPreview.contrastLevels = generateContrastLevels(score);
	}

	/**
	 * Generiert die Kontrastlevel nach WCAG für die verschiedenen Schriftgrößen
	 *
	 * @param score   der score, der mit dem Kontrast für die Farben erreicht wurde.
	 *
	 * @returns die Kontrastlevel für die verschiedenen Schriftgrößen
	 */
	function generateContrastLevels(score: string): string[] {
		const result: string[] = [];

		switch (score) {
			case "none":
				result.push(...Array(6).fill("< AA"));
				break;
			case "noneLargeAA":
				result.push(...Array(4).fill("< AA"));
				result.push(...Array(2).fill("AA"));
				break;
			case "AALargeAAA":
				result.push(...Array(4).fill("AA"));
				result.push(...Array(2).fill("AAA"));
				break;
			case "AAA":
				result.push(...Array(4).fill("AAA"));
				result.push(...Array(2).fill("AAA"));
				break;
		}

		return result;
	}

	/**
	 * Bestimmt den Farbwert einer Farbe anhand der Variable und des Themes
	 *
	 * @param variableName   die Farbvariable, deren RGB bestimmt werden soll.
	 * @param dark           false, wenn die Farbe im light-Mode bestimmt werden soll. True, wenn die Farbe im dark-Mode bestimmt werden soll.
	 *
	 * @returns der Farbwert
	 */
	function resolveComputedColor(variableName: string, dark = false): string {
		// Zur Bestimmung der Farbe muss eine dummy-div erstellt werden. Dieses ist nicht sichtbar.
		const dummy = document.createElement('div');
		dummy.style.display = 'none';
		dummy.style.setProperty('--color', `var(--color-${variableName})`);
		dummy.className = dark ? 'dark' : '';
		document.body.appendChild(dummy);

		const color = getComputedStyle(dummy).getPropertyValue('--color');
		const themeColor = resolveLightDark(color, dark);
		document.body.removeChild(dummy);
		return themeColor.trim();
	}

	/**
	 * Bestimmt aus einer "light-dark" Variablendefinition den entsprechenden RGB-Wert.
	 *
	 * @param input   der String mit der light-dark Variable
	 * @param dark    false, wenn die light-Mode Farbe bestimmt werden soll. True, denn die dark-Mode Farbe bestimmt werden soll.
	 *
	 * @returns die RGB-Definition der entsprechenden Farbe und des entsprechenden Themes
	 */
	function resolveLightDark(input: string, dark: boolean): string {
		if (!input.includes('light-dark'))
			return input;

		const match = /light-dark\(((?:[^(),]+|\([^()]*\))+),\s*((?:[^(),]+|\([^()]*\))+)\)/.exec(input);

		if (!match)
			return input;

		const [, lightValue, darkValue] = match;

		return dark ? darkValue.trim() : lightValue.trim();
	}

	/**
	 * Berechnet die zugehörige Hintergrundfarbe zu einer gegebenen Farbe.
	 *
	 * @param color   die Farbe, deren Hintergrundfabe bestimmt werden soll.
	 *
	 * @returns die Hintergrundfarbe zur gegebenen Vordergrundfarbe
	 */
	function getBackgroundColor(color: string): string {
		if (!color.includes('-on'))
			return 'bg-ui';

		return color
			.split('-')
			.map((part, index, parts) => {
				// Erster Teil wird 'bg'
				if (index === 0) return 'bg';

				// Das 'on' am Anfang des dritten Teils entfernen
				if (index === 2 && part.startsWith('on')) return part.replace(/^on/, '');

				// Wenn der String mit '-secondary-hover' endet, entferne '-secondary'
				if (index === parts.length - 2 && part === 'secondary' && parts[parts.length - 1] === 'hover') {
					return ''; // Entfernt 'secondary', wenn 'hover' direkt folgt
				}

				return part;
			})
			.filter(part => part !== '') // Entfernt leere Teile
			.join('-');
	}


	/**
	 * Bestimmt die Farbe für den Kontrastwert, abhängig von dem erreichten WCAG-Level.
	 *
	 * @param score   WCAG Level
	 *
	 * @returns die Textfarbe für das Kontrastlevel
	 */
	function getContrastColor(score: string): string {
		switch (score) {
			case "AAA":
				return "text-ui-success";
			case "AALargeAAA":
				return "text-ui-warning";
			default:
				return "text-ui-danger";
		}
	}

	/**
	 * Berechnen der relativen Leuchtdichte eines RGB-Farbwerts
	 * http://www.w3.org/TR/WCAG20/#contrast-ratiodef
	 *
	 * @param rgb   der RGB Farbwert
	 *
	 * @returns die relative Leuchtdichte
	 */
	function getContrastRelativeLuminance(rgb: { r: number, g: number, b: number }): number {
		const lowc = 1 / 12.92;
		const rsrgb = rgb.r / 255;
		const gsrgb = rgb.g / 255;
		const bsrgb = rgb.b / 255;
		//sRGB-Gammakorrektur
		const r = rsrgb <= 0.03928 ? rsrgb * lowc : Math.pow((rsrgb + 0.055) / 1.055, 2.4);
		const g = gsrgb <= 0.03928 ? gsrgb * lowc : Math.pow((gsrgb + 0.055) / 1.055, 2.4);
		const b = bsrgb <= 0.03928 ? bsrgb * lowc : Math.pow((bsrgb + 0.055) / 1.055, 2.4);
		return (r * 0.2126) + (g * 0.7152) + (b * 0.0722);
	}

	/**
	 * Berechnen des Kontrasts zwischen zwei Leuchtdichten
	 *
	 * @param background   Hintergrundfarbe
	 * @param foreground   Vordergrundfarbe
	 *
	 * @returns das Kontrastverhältnis
	 */
	function getContrastLuminance(background = 0, foreground = 0): number {
		const l1 = Math.max(background, foreground);
		const l2 = Math.min(background, foreground);
		return (l1 + 0.05) / (l2 + 0.05);
	}

	/**
	 * Konvertiert eine Farbkodierung in ein RGB-Objekt. Zulässige Farben sind HEX, RGB(A) und HSL(A).
	 *
	 * @param color   der Farbstring, der in ein RGB-Objekt umgewandelt werden soll.
	 *
	 * @returns { r: number; g: number; b: number }   ein Objekt mit den RGB-Werten
	 */
	function parseColorToRGB(color: string): { r: number; g: number; b: number } {
		if (typeof color !== 'string')
			return { r: 0, g: 0, b: 0 };

		color = color.trim().toLowerCase();

		const colorType = color.startsWith('#') ? 'hex' :
			color.startsWith('rgb') ? 'rgb' :
			color.startsWith('hsl') ? 'hsl' : 'unknown';

		switch (colorType) {
			case 'hex':
				return hexToRgb(color);
			case 'rgb':
				return rgbStringToRgb(color);
			case 'hsl':
				return hslToRgb(color);
			default:
				return { r: 0, g: 0, b: 0 };
		}
	}


	/**
	 * Wandelt einen HEX-Farbstring in RGB um.
	 *
	 * @param color   der Hex-Farbstring, der in ein RGB-Objekt umgewandelt werden soll.
	 *
	 * @returns { r: number; g: number; b: number }   ein Objekt mit den RGB-Werten
	 */
	function hexToRgb (color: string): { r: number; g: number; b: number }{
		// entfernt das '#' Zeichen
		let hex = color.slice(1);
		// Wandelt 3-stellige Hexadezimalwerte in 6-stellige um, indem die Buchstaben verdoppelt werden
		if (hex.length === 3)
			hex = hex.split('').map(c => c + c).join('');
		// Prüft, ob der Hexwert die richtige Zeichenlänge und die richtigen Zeichen beinhaltet.
		if (hex.length !== 6 || !/^[0-9a-f]{6}$/i.test(hex))
			return { r: 0, g: 0, b: 0 };
		// Wandelt den Hexadezimalwert in einen RGB-Wert um
		const bigint = parseInt(hex, 16);
		return { r: (bigint >> 16) & 255, g: (bigint >> 8) & 255, b: bigint & 255 };
	}

	/**
	 * Wandelt einen RGB-Farbstring in ein RGB-Objekt um.
	 *
	 * @param color   der RGB-Farbstring, der in ein RGB-Objekt umgewandelt werden soll.
	 *
	 * @returns { r: number; g: number; b: number }   ein Objekt mit den RGB-Werten
	 */
	function rgbStringToRgb (color: string): { r: number; g: number; b: number } {
		// Extrahiere die Zahlen aus dem RGB-Farbwert, der als string übergeben wird.
		const match = color.match(/\d+/g);
		// Prüft, ob ein gültiger Wert übergeben wurde
		if (!match || match.length < 3)
			return { r: 0, g: 0, b: 0 };
		const [r, g, b] = match.map(Number).slice(0, 3);
		return { r, g, b };
	}

	/**
	 * Wandelt einen HSL-Farbstring in ein RGB-Objekt um.
	 * Referenz: https://www.jameslmilner.com/posts/converting-rgb-hex-hsl-colors/
	 *
	 * @param color   der HSL-Farbstring, der in ein RGB-Objekt umgewandelt werden soll.
	 *
	 * @returns { r: number; g: number; b: number }   ein Objekt mit den RGB-Werten
	 */
	function hslToRgb(color: string): { r: number; g: number; b: number } {
		// Extrahiere die Zahlen aus dem HSL-Farbwert, der als string übergeben wird.
		const match = color.match(/[\d.]+/g);
		// Prüft, ob ein gültiger Wert übergeben wurde
		if (!match || match.length < 3)
			return { r: 0, g: 0, b: 0 };

		// Konvertiere die extrahierten Strings in Zahlen (h, s, l)
		const [h, s, l] = match.map(Number);

		// Normalisiere die HSL-Werte (sind üblicherweise in Prozent, daher /100)
		const hDecimal = h / 100;
		const sDecimal = s / 100;
		const lDecimal = l / 100;

		// Wenn die Sättigung (s) 0 ist, ist die Farbe ein Grauton.
		if (s === 0) {
			const gray = lDecimal * 255;
			// Grautöne haben alle RGB-Werte gleich (r, g, b sind gleich)
			return { r: gray, g: gray, b: gray };
		}

		// Hue-to-RGB Umrechnung (Hilfsfunktion, die den Wert von RGB für gegebene HSL-Werte berechnet)
		const hueToRGB = (p: number, q: number, tRaw: number): number => {
			let t = tRaw;
			// Bereinige t, wenn es außerhalb des [0, 1]-Bereichs liegt
			if (t < 0) t += 1;
			if (t > 1) t -= 1;

			// Berechne RGB basierend auf dem Wert von t (Hue)
			// Diese Berechnungen kommen aus den HSL-to-RGB Formeln
			if (t < (1 / 6)) return p + ((q - p) * 6 * t); // Farbton zwischen 0 und 60°
			if (t < (1 / 2)) return q; // Farbton zwischen 60° und 180°
			if (t < (2 / 3)) return p + ((q - p) * ((2 / 3) - t) * 6); // Farbton zwischen 180° und 240°
			return p; // Farbton zwischen 240° und 360°
		};

		// Berechne die Werte von q und p, die für die Umrechnung auf RGB verwendet werden
		const q = (lDecimal < 0.5)
			? (lDecimal * (1 + sDecimal)) // Wenn der Helligkeitswert weniger als 0.5 ist
			: (lDecimal + sDecimal - (lDecimal * sDecimal)); // Wenn der Helligkeitswert mehr als 0.5 ist

		// Berechne den Wert für p
		const p = (2 * lDecimal) - q;

		const r = hueToRGB(p, q, hDecimal + (1 / 3)); // Rot hat den Farbton verschoben um 1/3 (120°)
		const g = hueToRGB(p, q, hDecimal); // Grün entspricht dem Original Farbton
		const b = hueToRGB(p, q, hDecimal - (1 / 3)); // Blau hat den Farbton verschoben um -1/3 (-120°)

		// Gib die RGB-Werte zurück, skaliert auf den Bereich [0, 255]
		return {
			r: r * 255,
			g: g * 255,
			b: b * 255,
		};
	}

	/**
	 * Berechnet den Kontrastwert zwischen zwei Farben und gibt eine Bewertung zurück.
	 *
	 * @param color   die Farbe, deren Kontrast zur zugehörigen Hintergrundfarbe berechnet werden soll
	 * @param dark    false, wenn der Kontrast für den light-Mode berechnet werden soll. True, wenn der Kontrast für den dark-mode berechnet werden soll.
	 *
	 * @returns { contrastRatio: string, contrastLevel: string }   ein Objekt mit dem Kontrastverhältnis und dem Kontrastlevel
	 */
	function getContrast(color: string, dark: boolean) : { contrastRatio: string, contrastLevel: string } {
		const foreground = resolveComputedColor(color, dark);
		const background = resolveComputedColor(getBackgroundColor(color), dark);

		const fg = parseColorToRGB(foreground);
		const bg = parseColorToRGB(background);
		// Berechnen des Kontrastverhältnisses
		const contrast = getContrastLuminance(getContrastRelativeLuminance(bg), getContrastRelativeLuminance(fg));
		// Bewerten des Kontrastverhältnisses gemäß WCAG-Richtlinien
		let score ="";
		if (contrast >= 7)
			score ="AAA";
		else if (contrast >= 4.5)
			score ="AALargeAAA";
		else if (contrast >= 3)
			score ="noneLargeAA";
		else
			score = "none";
		return { contrastRatio: contrast.toFixed(2), contrastLevel: score };
	}

	/**
	 * Kopiert den Namen der Farbe in die Zwischenablage.
	 *
	 * @param color   Name der Farbe
	 */
	async function copyToClipboard(color: string) {
		try {
			await navigator.clipboard.writeText(color);
		} catch(e) {
			throw new DeveloperNotificationException("Beim Kopieren ist ein Fehler aufgetreten!");
		}
	}

</script>