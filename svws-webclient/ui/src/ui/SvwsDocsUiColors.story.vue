<template>
	<Story title="Farben (Design Tokens)" id="farben" group="top" icon="ri:palette-line" :responsive-disabled="true" :layout="{type: 'single'}" auto-props-disabled>
		<Variant title="Design Tokens" source=" " id="info">
			<div class="htw-prose dark:htw-prose-invert" style="max-width: unset;">
				<p><em>Weitere Informationen dazu sowie Links zu Tools und Resourcen sind unter den Farben auf dieser Seite aufgeführt.</em></p>
				<p>
					Um eine konsistente Farbgebung zu gewährleisten, verwenden wir ein Farbschema, das auf Design Tokens basiert. Die unten aufgeführten Tokens sind als CSS-Classes verfügbar und können in Komponenten und dem Client verwendet werden und sind semantisch nach diesen Attributen zusammengesetzt:
				</p>
				<table class="text-left w-150">
					<thead class="border-b border-ui h-10">
						<tr>
							<th>[type]</th>
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
			</div>
		</Variant>
		<Variant :title="`${type}-ui`" source="" :id="`tokens-${type}`" v-for="([type, typeMap]) in typeList" :key="type">
			<div class="grid grid-cols-2  items-center">
				<div v-for="(isDark, index) in [false, true]" :key="index">
					<div v-for="([role, roleColors]) in typeMap" :key="role" class="my-3" :class="{'dark': isDark}">
						<div v-for="color in roleColors" :key="color" class="ui-docs-bg flex items-center gap-1"
							:style="{
								/* Setzt die Hintergrundfarbe, wenn die Rolle mit 'on' beginnt */
								backgroundColor: color.includes('-on') ? `var(--color-bg-ui-${color.split('-on')[1]?.split('-')[0] || 'default'})` : 'var(--color-bg-ui)',
								/* Setzt die Textfarbe basierend auf Typ, Rolle, Prominenz und Interaktion */
								color: `${color.includes('-on') ? `var(--color-${color})` : `var(--color-text-ui)`}`,
								/* Setzt die Akzentfarbe, wenn der Typ mit 'accent' beginnt */
								accentColor: `${(type === 'accent') ? `var(--color-${color})` : ''}`,
							}">
							<div class="ui-docs-color-swatch"
								:style="{ backgroundColor: `var(--color-${color})`}" />
							<!-- Checkbox-Vorschau, falls Typ 'accent' ist -->
							<div v-if="type === 'accent'" class="ui-docs-preview-checkbox"><input type="checkbox" checked style="pointer-events: none; accent-color: inherit;"></div>
							<!-- Icon-Vorschau, falls Typ 'icon' ist -->
							<span v-if="type === 'icon'" class="i-ri-archive-line icon-xl min-w-6" :class="color" />
							<!-- Text mit Farbvariablenbezeichnung -->
							<div>
								<span v-for="(part, partIndex) in color.split('-')" :key="partIndex" class="font-mono"
									:class="{'font-bold': partIndex < 2 , 'opacity-50':(( part === 'ui') || (part === 'uistatic'))}">
									{{ part }}<span v-if="partIndex < color.split('-').length - 1">-</span>
								</span>
							</div>
							<div class="contrast-value text-ui" />
						</div>
					</div>
				</div>
			</div>
		</Variant>
		<!-- Iteration über die semantischen types ['bg', 'text', 'border', 'accent', 'ring', 'icon'] -->
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


	const textColorMap: Map<string, string[]> = new Map([
		['default', [
			'text-ui', 'text-ui-0', 'text-ui-10', 'text-ui-25',
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
			'text-ui-caution', 'text-ui-caution-hover',
			'text-ui-caution-secondary', 'text-ui-caution-secondary-hover',
		]],
		['neutral', [
			'text-ui-neutral', 'text-ui-neutral-hover',
			'text-ui-neutral-secondary', 'text-ui-neutral-secondary-hover',
		]],
		['disabled', [
			'text-ui-disabled', 'text-ui-disabled-secondary',
		]],
		['onbrand', [
			'text-ui-onbrand', 'text-ui-onbrand-hover',
			'text-ui-onbrand-secondary', 'text-ui-onbrand-secondary-hover',
		]],
		['onstatistic', [
			'text-ui-onstatistic', 'text-ui-onstatistic-hover',
			'text-ui-onstatistic-secondary', 'text-ui-onstatistic-secondary-hover',
		]],
		['onselected', [
			'text-ui-onselected', 'text-ui-onselected-hover',
			'text-ui-onselected-secondary', 'text-ui-onselected-secondary-hover',
		]],
		['ondanger', [
			'text-ui-ondanger', 'text-ui-ondanger-hover',
			'text-ui-ondanger-secondary', 'text-ui-ondanger-secondary-hover',
		]],
		['onsuccess', [
			'text-ui-onsuccess', 'text-ui-onsuccess-hover',
			'text-ui-onsuccess-secondary', 'text-ui-onsuccess-secondary-hover',
		]],
		['onwarning', [
			'text-ui-onwarning', 'text-ui-onwarning-hover',
			'text-ui-onwarning-secondary', 'text-ui-onwarning-secondary-hover',
		]],
		['oncaution', [
			'text-ui-oncaution', 'text-ui-oncaution-hover',
			'text-ui-oncaution-secondary', 'text-ui-oncaution-secondary-hover',
		]],
		['onneutral', [
			'text-ui-onneutral', 'text-ui-onneutral-hover',
			'text-ui-onneutral-secondary', 'text-ui-onneutral-secondary-hover',
		]],
		['ondisabled', [
			'text-ui-ondisabled', 'text-ui-ondisabled-secondary',
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
		['onbrand', ['icon-ui-onbrand', 'icon-ui-onbrand-hover', 'icon-ui-onbrand-secondary', 'icon-ui-onbrand-secondary-hover']],
		['onstatistic', ['icon-ui-onstatistic', 'icon-ui-onstatistic-hover', 'icon-ui-onstatistic-secondary', 'icon-ui-onstatistic-secondary-hover']],
		['onselected', ['icon-ui-onselected', 'icon-ui-onselected-hover', 'icon-ui-onselected-secondary', 'icon-ui-onselected-secondary-hover']],
		['ondanger', ['icon-ui-ondanger', 'icon-ui-ondanger-hover', 'icon-ui-ondanger-secondary', 'icon-ui-ondanger-secondary-hover']],
		['onsuccess', ['icon-ui-onsuccess', 'icon-ui-onsuccess-hover', 'icon-ui-onsuccess-secondary', 'icon-ui-onsuccess-secondary-hover']],
		['onwarning', ['icon-ui-onwarning', 'icon-ui-onwarning-hover', 'icon-ui-onwarning-secondary', 'icon-ui-onwarning-secondary-hover']],
		['oncaution', ['icon-ui-oncaution', 'icon-ui-oncaution-hover', 'icon-ui-oncaution-secondary', 'icon-ui-oncaution-secondary-hover']],
		['onneutral', ['icon-ui-onneutral', 'icon-ui-onneutral-hover', 'icon-ui-onneutral-secondary', 'icon-ui-onneutral-secondary-hover']],
		['ondisabled', ['icon-ui-ondisabled', 'icon-ui-ondisabled-secondary']],
		['uistatic', ['icon-uistatic', 'icon-uistatic-0', 'icon-uistatic-10', 'icon-uistatic-25', 'icon-uistatic-50', 'icon-uistatic-75', 'icon-uistatic-100']],
	]);

	const shadowColorMap: Map<string, string[]> = new Map([
		['default', ['shadow-ui-0', 'shadow-ui-10', 'shadow-ui-25', 'shadow-ui-50', 'shadow-ui-75', 'shadow-ui-100'	]],
	]);




	const typeList = new Map([["bg", backgroundColorMap], ["text", textColorMap], ['border', borderColorMap], ['accent', accentColorMap],
		['ring', ringColorMap], ['icon', iconColorMap], ['shadow', shadowColorMap]]);


	const semantics = {
		type: ['bg', 'text', 'border', 'accent', 'ring', 'icon'],
		role: ['', 'brand', 'statistic', 'selected', 'danger', 'success', 'warning', 'caution', 'neutral', 'disabled', 'onbrand', 'onstatistic', 'onselected', 'ondanger', 'onsuccess', 'onwarning', 'oncaution', 'onneutral', 'ondisabled'],
		prominence: ['', 'secondary', '0', '10', '25', '50', '75', '100'], // strong, weak
		interaction: ['', 'hover'], // active
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
			valueTarget.textContent = docsGetContrast(bg, fg);
		});
	}

	onMounted(() => {
		updateContrastValues();
	});

</script>