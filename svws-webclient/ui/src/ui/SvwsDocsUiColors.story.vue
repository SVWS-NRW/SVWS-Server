<template>
	<Story title="Farben (Design Tokens)" id="farben" group="top" icon="ri:palette-line" :responsive-disabled="true" :layout="{type: 'grid', width: '90%'}" auto-props-disabled>
		<Variant title="Design Tokens" source=" " id="info">
			<div class="htw-prose dark:htw-prose-invert" style="max-width: unset;">
				<p><em>Weitere Informationen dazu sowie Links zu Tools und Resourcen sind unter den Farben auf dieser Seite aufgeführt.</em></p>
				<p>
					Um eine konsistente Farbgebung zu gewährleisten, verwenden wir ein Farbschema, das auf Design Tokens basiert. Die unten aufgeführten Tokens sind als CSS-Classes verfügbar und können in Komponenten und dem Client verwendet werden und sind semantisch nach diesen Attributen zusammengesetzt:
				</p>
				<table style="max-width: 60ch;">
					<thead>
						<tr>
							<th>[type]</th>
							<th>-ui</th>
							<th><em>-[role]</em></th>
							<th><em>-[prominence]</em></th>
							<th><em>-[interaction]</em></th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>
								<div v-for="(type, typeIndex) in semantics.type" :key="typeIndex">{{ type }}</div>
							</td>
							<td><strong><span class="opacity-50">-ui</span></strong></td>
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
			</div>
		</Variant>
		<!-- Iteration über die semantischen types ['bg', 'text', 'border', 'accent', 'ring', 'icon'] -->
		<Variant :title="`${type}-ui`" source=" " :id="`tokens-${type}`" v-for="(type, typeIndex) in semantics.type" :key="typeIndex">
			<div class="relative">
				<div class="flex flex-col gap-4 font-mono">
					<div v-for="(role, roleIndex) in semantics.role" :key="roleIndex" style="padding: 0.5em 0;">
						<div v-for="(prominence, prominenceIndex) in semantics.prominence" :key="prominenceIndex">
							<div v-for="(interaction, interactionIndex) in semantics.interaction" :key="interactionIndex">
								<div class="grid grid-cols-2 gap-1 items-center">
									<!-- Filterung plausibler UI Elemente -->
									<template v-if="shouldExist(type, role, prominence, interaction)">
										<!-- light mode -->
										<div class="ui-docs-bg flex items-center gap-1"
											:style="{
												/* Setzt die Hintergrundfarbe, wenn die Rolle mit 'on' beginnt */
												backgroundColor: `${role.startsWith('on') ? `var(--color-bg-ui-${role.replace('on', '')}${ prominence ? `-${prominence}` : '' }${ interaction ? `-${interaction}` : '' })` : ''}`,
												/* Setzt die Textfarbe basierend auf Typ, Rolle, Prominenz und Interaktion */
												color: `${role.startsWith('on') ? `var(--color-${type.startsWith('icon') ? 'text' : type}-ui${ role ? `-${role}` : '' }${ prominence ? `-${prominence}` : '' }${ interaction ? `-${interaction}` : '' })` : ''}`,
												/* Setzt die Akzentfarbe, wenn der Typ mit 'accent' beginnt */
												accentColor: `${type.startsWith('accent') ? `var(--color-accent-ui${ role ? `-${role}` : '' })` : ''}`
											}">
											<div class="ui-docs-color-swatch" :style="{backgroundColor: `var(--color-${type === 'icon' ? 'text' : type}-ui${ role ? `-${role}` : '' }${ prominence ? `-${prominence}` : '' }${ interaction ? `-${interaction}` : '' })`}" />
											<!-- Checkbox-Vorschau, falls Typ 'accent' ist -->
											<div v-if="type === 'accent'" class="ui-docs-preview-checkbox"><input type="checkbox" checked style="pointer-events: none; accent-color: inherit;"></div>
											<!-- Icon-Vorschau, falls Typ 'icon' ist -->
											<span v-if="type === 'icon'" class="i-ri-archive-line icon-xl" :class="`icon-ui${ role ? `-${role}` : '' }`" />
											<!-- Text mit Farbvariablenbezeichnung -->
											<div style="user-select: all;">
												<span><span class="font-bold">{{ type }}</span><span class="opacity-50 font-bold">-ui</span></span>
												<span>{{ role ? `-${role}` : '' }}</span>
												<span>{{ prominence ? `-${prominence}` : '' }}</span>
												<span>{{ interaction ? `-${interaction}` : '' }}</span>
											</div>
											<div class="contrast-value" />
										</div>
										<!-- dark mode -->
										<div class="ui-docs-bg dark flex items-center gap-1 bg-ui-danger-secondary-hover"
											:class="{darkOnDisabled: role.startsWith('ondisabled')}"
											:style="{
												/* Setzt die Hintergrundfarbe, wenn die Rolle mit 'on' beginnt */
												backgroundColor: `${role.startsWith('on') ? `var(--color-bg-ui-${role.replace('on', '')}${ prominence ? `-${prominence}` : '' }${ interaction ? `-${interaction}` : '' })` : ''}`,
												/* Setzt die Textfarbe basierend auf Typ, Rolle, Prominenz und Interaktion */
												color: `${role.startsWith('on') ? `var(--color-${type.startsWith('icon') ? 'text' : type}-ui${ role ? `-${role}` : '' }${ prominence ? `-${prominence}` : '' }${ interaction ? `-${interaction}` : '' })` : ''}`,
												/* Setzt die Akzentfarbe, wenn der Typ mit 'accent' beginnt */
												accentColor: `${type.startsWith('accent') ? `var(--color-accent-ui${ role ? `-${role}` : '' })` : ''}`
											}">
											<div class="ui-docs-color-swatch" :style="{backgroundColor: `var(--color-${type === 'icon' ? 'text' : type}-ui${ role ? `-${role}` : '' }${ prominence ? `-${prominence}` : '' }${ interaction ? `-${interaction}` : '' })`}" />
											<!-- Checkbox-Vorschau, falls Typ 'accent' ist -->
											<div v-if="type === 'accent'" class="ui-docs-preview-checkbox"><input type="checkbox" checked style="pointer-events: none;"></div>
											<!-- Icon-Vorschau, falls Typ 'icon' ist -->
											<span v-if="type === 'icon'" class="i-ri-archive-line icon-xl" :class="`icon-ui${ role ? `-${role}` : '' }`" />
											<!-- Text mit Farbvariablenbezeichnung -->
											<div style="user-select: all;">
												<span><span class="font-bold">{{ type }}</span><span class="opacity-50 font-bold">-ui</span></span>
												<span>{{ role ? `-${role}` : '' }}</span>
												<span>{{ prominence ? `-${prominence}` : '' }}</span>
												<span>{{ interaction ? `-${interaction}` : '' }}</span>
											</div>
											<div class="contrast-value" />
										</div>
									</template>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- Generate all tailwind colors for icons:
			 icon-ui icon-ui-brand icon-ui-statistic icon-ui-danger icon-ui-success icon-ui-warning icon-ui-caution icon-ui-neutral icon-ui-onbrand icon-ui-onstatistic icon-ui-onselected icon-ui-ondanger icon-ui-onsuccess icon-ui-onwarning icon-ui-oncaution icon-ui-onneutral
			 icon-ui--dark icon-ui-brand--dark icon-ui-statistic--dark icon-ui-danger--dark icon-ui-success--dark icon-ui-warning--dark icon-ui-caution--dark icon-ui-neutral--dark icon-ui-onbrand--dark icon-ui-onstatistic--dark icon-ui-onselected--dark icon-ui-ondanger--dark icon-ui-onsuccess--dark icon-ui-onwarning--dark icon-ui-oncaution--dark icon-ui-onneutral--dark -->
		</Variant>
		<Variant title="Beispiele" source=" " id="beispiele">
			<div class="htw-prose dark:htw-prose-invert" style="max-width: unset;">
				<table>
					<thead>
						<tr>
							<th>Deprecated</th>
							<th>Neue Design Tokens</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>
								<div><code>bg-light</code></div>
								<div><code>dark:bg-dark</code></div>
							</td>
							<td><code>bg-ui-neutral</code></td>
						</tr>
						<tr>
							<td>
								<div><code>bg-white</code></div>
								<div><code>dark:bg-black</code></div>
								<div><code>text-black</code></div>
								<div><code>dark:text-white</code></div>
								<div><code>hover:text-primary</code></div>
								<div><code>dark:hover:text-primary-dark</code></div>
							</td>
							<td>
								<div><code>bg-ui</code></div>
								<div><code>text-ui</code></div>
								<div><code>hover:text-ui-hover</code></div>
							</td>
						</tr>
						<tr>
							<td>
								<div><code>bg-white</code></div>
								<div><code>dark:bg-black</code></div>
								<div><code>text-red-500</code></div>
								<div><code>dark:text-red-400</code></div>
								<div><code>border-red-500</code></div>
								<div><code>dark:border-red-400</code></div>
								<div><code>hover:bg-red-500</code></div>
								<div><code>dark:hover:bg-red-400</code></div>
								<div><code>hover:text-white</code></div>
								<div><code>dark:hover:text-black</code></div>
								<div><code>focus-visible:bg-red-500</code></div>
								<div><code>dark:focus-visible:bg-red-400</code></div>
								<div><code>focus-visible:ring-red-500/25</code></div>
								<div><code>dark:focus-visible:ring-red-400/25</code></div>
							</td>
							<td>
								<div><code>bg-ui</code></div>
								<div><code>text-ui-danger</code></div>
								<div><code>border-ui-danger</code></div>
								<div><code>hover:bg-ui-danger</code></div>
								<div><code>hover:text-ui-ondanger</code></div>
								<div><code>focus-visible:bg-ui-danger</code></div>
								<div><code>focus-visible:text-ui-ondanger</code></div>
								<div><code>focus-visible:ring-ui-danger</code></div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</Variant>
		<Variant title="Weitere Informationen" source=" " id="resources">
			<div class="htw-prose dark:htw-prose-invert">
				<h2>SVWS UI Components und Apps</h2>
				<p>Es sollen ausschließlich die hier aufgeführten Tokens verwendet werden. Die alten CSS-Color-Classes (Tailwind defaults) sollten nicht mehr verwendet werden. Sie sind nur noch in alten Komponenten und Designs vorhanden und werden ersetzt.</p>
				<h3>Darkmode</h3>
				<p>Durch die Verwendung dieser Tokens können wir sicherstellen, dass die Farben in allen Anwendungen und Komponenten konsistent sind und sich einfach global anpassen lassen. Auch z.B. der Darkmode wird so ohne separate Gestaltung für jede Farbe automatisch unterstützt und ist optimiert.</p>
				<h3>Erweiterungen</h3>
				<p>Bei Bedarf kann das System durch weitere Farben bzw. Farbabstufungen und -kombinationen erweitert werden. In den meisten Fällen sollte für jede Anforderung jedoch eine passende Kombination aus den vorhandenen Tokens gefunden werden können.</p>
				<hr>
				<h2>Barrierefreiheit nach WCAG 2</h2>
				<p>Für normale Texte wird versucht mindestens AA >=4,5:1 einzuhalten, bei größeren UI-Elementen und Hintergründen sind auch >=3:1 möglich. In speziellen Fällen, wie Borders, secondary Texte bzw. Icons, kann das Kontrastverhältnis je nach Kontext geringer ausfallen. Besonders für deaktivierte Elemente wird der AA-Standard nicht immer angewendet, da diese weniger prominent sein sollen.</p>
				<p>In Zukunft kann ein weiteres Farbschema neben Light und Dark Mode ergänzt werden, welches AAA >=7:1 priorisiert.</p>
				<ul>
					<li><a href="https://www.w3.org/WAI/WCAG21/Understanding/conformance#levels" target="_blank" rel="noopener noreferrer">Understanding Levels of Conformance (AA, AAA)</a></li>
					<li><a href="https://www.w3.org/WAI/WCAG21/Understanding/contrast-minimum.html" target="_blank" rel="noopener noreferrer">Contrast (Minimum) (Level AA)</a></li>
					<li><a href="https://www.w3.org/WAI/WCAG21/Understanding/contrast-enhanced.html" target="_blank" rel="noopener noreferrer">Contrast (Enhanced) (Level AAA)</a></li>
					<li><a href="https://www.w3.org/WAI/WCAG22/Understanding/non-text-contrast.html" target="_blank" rel="noopener noreferrer">Non-text Contrast</a></li>
				</ul>
				<h2>Kontraste prüfen</h2>
				<p>Hinter jeder Farbe wird hier in den Docs der entsprechende Kontrastwert angezeigt, basierend auf den Algorithmen zur Contrast Ratio und Relative Luminance. Diese Berechnungen folgen den Vorgaben der WCAG.</p>
				<ul>
					<li><a href="https://www.w3.org/TR/WCAG21/#dfn-contrast-ratio" target="_blank" rel="noopener noreferrer">Contrast Ratio</a></li>
					<li><a href="https://www.w3.org/TR/WCAG21/#dfn-relative-luminance" target="_blank" rel="noopener noreferrer">Relative Luminance</a></li>
				</ul>
				<p>Info zur Kennzeichnung: (AA/AAA) Das jeweilige Contrast Level ist erreicht. (!) Geringer Kontrast von weniger als 3:1, aber in bestimmten Fällen beabsichtigt (s. oben).</p>
				<p>Mit verschiedenen Tools können Farben auch einzeln getestet werden:</p>
				<ul>
					<li><a href="https://color.adobe.com/create/color-contrast-analyzer" target="_blank" rel="noopener noreferrer">Adobe Color Accessibility Tools: Contrast Checker</a></li>
					<li><a href="https://medium.com/@NateBaldwin/leonardo-an-open-source-contrast-based-color-generator-92d61b6521d2" target="_blank" rel="noopener noreferrer">Leonardo: an open source contrast-based color generator</a></li>
					<li><a href="https://leonardocolor.io/tools.html#" target="_blank" rel="noopener noreferrer">Leonardo Color Toolbox (Contrast Check)</a></li>
					<li><a href="https://leonardocolor.io/theme.html#" target="_blank" rel="noopener noreferrer">Leonardo Adaptive color theme tool</a></li>
				</ul>
				<h2>Farbschema und semantische Bezeichnungen</h2>
				<p>Zur Erstellung des Farbschemas wurden Best Practices anderer Apps analysiert. Unter Berücksichtigung der Anforderungen aus dem SVWS-Client und der UI-Komponenten wurde das System mit Design Tokens definiert. </p>
				<p>Damit das Farbsystem flexibel und erweiterbar bleibt, wurden die Tokens semantisch nach Type, Role, Prominenz und Interaktion aufgeteilt. Damit ist es möglich das System für neue Komponenten und Features sinnvoll zu erweitern und sogar komplett neue Themes (z.B. High Contrast) unkompliziert zu ergänzen.</p>
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
				<h2>Technische Umsetzung</h2>
				<p>Farben sind in <code>/SVWS-Server/svws-webclient/ui/src/tailwind/tailwind.preset.js</code> definiert. Eine allgemeine Palette ist darin unter <code>colors > ui > palette</code> definiert. Diese Farben sollen niemals direkt verwendet werden. <a href="https://tailwindcss.com/docs/theme#configuration-reference" target="_blank" rel="noopener noreferrer">Tailwind Configuration Reference (separate Definition für Border, Text, Background, Ring colors)</a></p>
				<p>
					In <code>/SVWS-Server/svws-webclient/ui/src/assets/styles/colors.css</code> werden die Farben für jeden Token angepasst. Dazu sollen ausschließlich Farben aus der UI Palette eingesetzt werden.
					<br>Im <code>:root</code> ist der Default bzw. Light Mode angelegt. Unter der <code>.dark</code> css class werden in diesem Fall alle Tokens für den Dark Mode überschrieben. Bei Erweiterung durch zum Beispiel einen High Contrast Mode muss nur ein Block mit allen Tokens dupliziert werden und kann ohne Kollision separat und sicher angepasst werden.
				</p>
			</div>
		</Variant>
	</Story>
</template>

<script setup lang="ts">

	import { onMounted } from 'vue';

	const semantics = {
		type: ['bg', 'text', 'border', 'accent', 'ring', 'icon'],
		role: ['', 'brand', 'statistic', 'selected', 'danger', 'success', 'warning', 'caution', 'neutral', 'disabled', 'onbrand', 'onstatistic', 'onselected', 'ondanger', 'onsuccess', 'onwarning', 'oncaution', 'onneutral', 'ondisabled', 'inverted'],
		prominence: ['', 'secondary', 'weak'], // strong, weak
		interaction: ['', 'hover'], // active
	}

	function shouldExist(type = '', role = '', prominence = '', interaction = '') {
		if ((type === 'bg' && prominence === 'weak' && interaction === '' && !role.startsWith('on') && role !== 'disabled' && role !== 'inverted' && role !== 'selected' && role !== ''))
			return true;
		if ((type === 'bg' && role !== '' && role.startsWith('on')) || (type === 'bg' && prominence !== '') || (role.includes('disabled') && interaction !== ''))
			return false;
		if ((type === 'ring') && (role === 'selected' || role.includes('disabled') || role.startsWith('on') || prominence !== '' || interaction !== ''))
			return false;
		if ((type === 'icon') && (role === 'selected' || role.includes('disabled') || prominence !== '' || interaction !== ''))
			return false;
		if ((type === 'accent') && (role === 'selected' || prominence !== '' || interaction !== ''))
			return false;
		if ((type !== 'bg' && role === 'inverted') || (type === 'bg' && role === 'inverted' && interaction !== '') || (type !== 'bg' && prominence === 'weak'))
			return false;
		return true;
	}

	// http://www.w3.org/TR/WCAG20/#contrast-ratiodef
	// Berechnen der relativen Leuchtdichte eines RGB-Farbwerts
	function docsContrastRelativeLuminance(rgb = [0, 0, 0]) {
		const lowc = 1 / 12.92;
		const rsrgb = rgb[0] / 255;
		const gsrgb = rgb[1] / 255;
		const bsrgb = rgb[2] / 255;
		//sRGB-Gammakorrektur
		const r = rsrgb <= 0.03928 ? rsrgb * lowc : Math.pow((rsrgb + 0.055) / 1.055, 2.4);
		const g = gsrgb <= 0.03928 ? gsrgb * lowc : Math.pow((gsrgb + 0.055) / 1.055, 2.4);
		const b = bsrgb <= 0.03928 ? bsrgb * lowc : Math.pow((bsrgb + 0.055) / 1.055, 2.4);
		return (r * 0.2126) + (g * 0.7152) + (b * 0.0722);
	}

	// Berechnen des Kontrasts zwischen zwei Leuchtdichten
	function docsContrastLuminance(a = 0, b = 0) {
		const l1 = Math.max(a, b); // Helle Farbe
		const l2 = Math.min(a, b); // Dunkle Farbe
		return (l1 + 0.05) / (l2 + 0.05);
	}

	// Berechnen des Kontrastwertes zwischen zwei Farben und gibt eine Bewertung zurück
	function docsGetContrast(a = "", b = "") {
		if (a === "" || b === "")
			return "N/A"; // Falls Farben fehlen
		// Extrahieren der RGB-Werte aus CSS-Farbstrings
		const bg = a.match(/\d+/g)?.map(Number);
		const fg = b.match(/\d+/g)?.map(Number);
		// Berechnen des Kontrastverhältnisses
		const contrast = docsContrastLuminance(docsContrastRelativeLuminance(bg), docsContrastRelativeLuminance(fg));
		// Bewerten des Kontrastverhältnisses gemäß WCAG-Richtlinien
		const score = contrast >= 7 ? "AAA" : contrast >= 4.5 ? "AA" : contrast >= 3 ? "" : "!";
		if (contrast < 1.09)
			return; // Falls der Kontrast zu niedrig ist, wird nichts zurückgegeben
		return `${contrast.toFixed(2)}:1${score !== "" ? ` (${score})` : ""}`;
	}

	// Aktualisieren der Kontrastwerte in der UI
	function updateContrastValues() {
		document.querySelectorAll('.ui-docs-bg').forEach((el) => {
			const swatch = el.querySelector('.ui-docs-color-swatch');
			const valueTarget = el.querySelector('.contrast-value');
			if (!swatch || !valueTarget)
				return;
			const bg = window.getComputedStyle(el).backgroundColor;
			const fg = window.getComputedStyle(swatch).backgroundColor;
			valueTarget.textContent = docsGetContrast(bg, fg) ?? '';
		});
	}

	onMounted(() => {
		updateContrastValues();
	});

</script>