<template>
	<Story title="Icons" id="icons" group="top" icon="ri:landscape-line" :responsive-disabled="true" :layout="{type: 'grid', width: '45%'}">
		<Variant title="Größen und Farben" source=" ">
			<table class="svws-docs-icon-table">
				<tbody>
					<tr v-for="size in ['xxs', 'xs', 'sm', 'default', 'lg', 'xl', 'xxl']" :key="size" style="grid-template-columns: 1fr 1fr 2fr;" :class="size === 'default' ? 'bg-ui-selected text-ui-onselected' : ''">
						<th><span class="icon i-ri-archive-line" :class="size === 'default' ? 'icon icon-ui-onselected' : `icon-${size}`" /></th>
						<td>{{ iconSizes[size as keyof typeof iconSizes] }}</td>
						<td><code>icon{{ size !== 'default' ? `-${size}` : '' }}</code></td>
					</tr>
				</tbody>
			</table>
			<!-- Generate all icon sizes: icon-xxs icon-xs icon-sm icon icon-lg icon-xl icon-xxl -->
			<div class="prose mt-12">
				<div class="flex gap-1 flex-wrap -mb-3">
					<template v-for="color in iconColors" :key="color">
						<span class="icon-xl i-ri-palette-line" :class="`icon-${color}`" />
					</template>
				</div>
				<p>Übersicht aller Varianten: <a href="/story/farben?variantId=tokens-icon">Farben (Design Tokens)</a>.</p>
			</div>
		</Variant>
		<Variant title="App Icons" source=" ">
			<table class="svws-docs-icon-table">
				<tbody>
					<tr>
						<th><span class="icon i-ri-school-line" /></th>
						<td>Schule</td>
						<td><code>{{ '<' }}span class="icon i-ri-school-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-archive-line" /></th>
						<td>Kataloge</td>
						<td><code>{{ '<' }}span class="icon i-ri-archive-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-group-line" /></th>
						<td>Schüler</td>
						<td><code>{{ '<' }}span class="icon i-ri-group-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-briefcase-line" /></th>
						<td>Lehrer</td>
						<td><code>{{ '<' }}span class="icon i-ri-briefcase-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-team-line" /></th>
						<td>Klassen</td>
						<td><code>{{ '<' }}span class="icon i-ri-team-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-book-2-line" /></th>
						<td>Kurse</td>
						<td><code>{{ '<' }}span class="icon i-ri-book-2-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-graduation-cap-line" /></th>
						<td>Oberstufe</td>
						<td><code>{{ '<' }}span class="icon i-ri-graduation-cap-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-bar-chart-2-line" /></th>
						<td>Statistik</td>
						<td><code>{{ '<' }}span class="icon i-ri-bar-chart-2-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-calendar-event-line" /></th>
						<td>Stundenplan</td>
						<td><code>{{ '<' }}span class="icon i-ri-calendar-event-line"{{ ' />' }}</code></td>
					</tr>
				</tbody>
			</table>
			<table class="svws-docs-icon-table">
				<tbody>
					<tr>
						<th><span class="icon i-ri-logout-circle-line" /></th>
						<td>Logout</td>
						<td><code>{{ '<' }}span class="icon i-ri-logout-circle-line"{{ ' />' }}</code></td>
					</tr>
					<!--<tr>
						<th><span class="icon i-ri-palette-line" /></th>
						<td>Ansicht (Einstellungen)</td>
						<td><code>{{ '<' }}span class="icon i-ri-palette-line"{{ ' />' }}</code></td>
					</tr>-->
				</tbody>
			</table>
		</Variant>
		<Variant title="Häufig verwendet" source=" ">
			<table class="svws-docs-icon-table">
				<tbody>
					<tr>
						<th><span class="icon i-ri-add-line" /></th>
						<td>Hinzufügen, neuer Datensatz</td>
						<td><code>{{ '<' }}span class="icon i-ri-add-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-edit-2-line" /></th>
						<td>Bearbeiten</td>
						<td><code>{{ '<' }}span class="icon i-ri-edit-2-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-delete-bin-line" /></th>
						<td>Löschen</td>
						<td><code>{{ '<' }}span class="icon i-ri-delete-bin-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-link" /></th>
						<td>Link zu einem anderen Bereich im Client</td>
						<td><code>{{ '<' }}span class="icon i-ri-link"{{ ' />' }}</code></td>
					</tr>
				</tbody>
			</table>
			<table class="svws-docs-icon-table">
				<tbody>
					<tr>
						<th><span class="icon i-ri-question-line" /></th>
						<td>Hilfe/Information, Tooltip Trigger</td>
						<td><code>{{ '<' }}span class="icon i-ri-question-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-information-line" /></th>
						<td>Information, Tooltip Trigger</td>
						<td><code>{{ '<' }}span class="icon i-ri-information-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-alert-line" /></th>
						<td>Warnung</td>
						<td><code>{{ '<' }}span class="icon i-ri-alert-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-error-warning-line" /></th>
						<td>Alternative Warnung/Fehler</td>
						<td><code>{{ '<' }}span class="icon i-ri-error-warning-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-alert-fill" /></th>
						<td>Fehler, Steigerung von "Warnung"</td>
						<td><code>{{ '<' }}span class="icon i-ri-alert-fill"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-checkbox-circle-fill" /></th>
						<td>Keine Fehler, gültig</td>
						<td><code>{{ '<' }}span class="icon i-ri-checkbox-circle-fill"{{ ' />' }}</code></td>
					</tr>
				</tbody>
			</table>
		</Variant>
		<Variant title="Weitere Icons" source=" ">
			<table class="svws-docs-icon-table">
				<tbody>
					<tr>
						<th><span class="icon i-ri-eye-line" /></th>
						<td>Element einblenden</td>
						<td><code>{{ '<' }}span class="icon i-ri-eye-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-eye-off-line" /></th>
						<td>Element ausblenden</td>
						<td><code>{{ '<' }}span class="icon i-ri-eye-off-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-printer-line" /></th>
						<td>Drucken</td>
						<td><code>{{ '<' }}span class="icon i-ri-printer-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-sparkling-line" /></th>
						<td>Automatisierung, z. B. Verteilen von Kursen</td>
						<td><code>{{ '<' }}span class="icon i-ri-sparkling-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-upload-2-line" /></th>
						<td>Importieren, Hochladen</td>
						<td><code>{{ '<' }}span class="icon i-ri-upload-2-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-download-2-line" /></th>
						<td>Exportieren, Herunterladen</td>
						<td><code>{{ '<' }}span class="icon i-ri-download-2-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-shield-star-line" /></th>
						<td>Admin-Rechte, Administrative Benutzergruppe</td>
						<td><code>{{ '<' }}span class="icon i-ri-shield-star-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-bug-fill" /></th>
						<td>Fehlermeldung Bug</td>
						<td><code>{{ '<' }}span class="icon i-ri-bug-fill"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-arrow-down-s-line" /></th>
						<td>Zugeklappt Pfeil</td>
						<td><code>{{ '<' }}span class="icon i-ri-arrow-down-s-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-arrow-up-s-line" /></th>
						<td>Ausgeklappt Pfeil</td>
						<td><code>{{ '<' }}span class="icon i-ri-arrow-up-s-line"{{ ' />' }}</code></td>
					</tr>
				</tbody>
			</table>
		</Variant>
		<Variant title="Oberstufe" source=" ">
			<table class="svws-docs-icon-table">
				<tbody>
					<tr>
						<th><span class="icon i-ri-spam-3-line" /></th>
						<td>Nichtverteilt</td>
						<td><code>{{ '<' }}span class="icon i-ri-spam-3-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-alert-line" /></th>
						<td>Kollision</td>
						<td><code>{{ '<' }}span class="icon i-ri-alert-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-error-warning-fill" /></th>
						<td>Kollision und Nichtverteilt</td>
						<td><code>{{ '<' }}span class="icon i-ri-error-warning-fill"{{ ' />' }}</code></td>
					</tr>
				</tbody>
			</table>
			<table class="svws-docs-icon-table">
				<tbody>
					<tr>
						<th><span class="icon i-ri-draft-line" /></th>
						<td>Fachwahl: Schriftlich</td>
						<td><code>{{ '<' }}span class="icon i-ri-draft-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-chat1-line" /></th>
						<td>Fachwahl: Mündlich</td>
						<td><code>{{ '<' }}span class="icon i-ri-chat1-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-settings-3-line" /></th>
						<td>Regeln zur Blockung</td>
						<td><code>{{ '<' }}span class="icon i-ri-settings-3-line"{{ ' />' }}</code></td>
					</tr>
				</tbody>
			</table>
			<table class="svws-docs-icon-table">
				<tbody>
					<tr>
						<th><span class="icon i-ri-loop-right-line" /></th>
						<td>Laufbahnplanung: Modus wechseln (Normal, Manuell, Hochschreiben)</td>
						<td><code>{{ '<' }}span class="icon i-ri-loop-right-line"{{ ' />' }}</code></td>
					</tr>
				</tbody>
			</table>
		</Variant>
		<Variant title="Input Fields und Interaktion" source=" ">
			<table class="svws-docs-icon-table">
				<tbody>
					<tr>
						<th><span class="icon i-ri-draggable" /></th>
						<td>Drag & Drop Handle</td>
						<td><code>{{ '<' }}span class="icon i-ri-draggable"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-expand-up-down-fill" /></th>
						<td>Multiselect Input</td>
						<td><code>{{ '<' }}span class="icon i-ri-expand-up-down-fill"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-expand-up-down-line" /></th>
						<td>Headless Multiselect Input</td>
						<td><code>{{ '<' }}span class="icon i-ri-expand-up-down-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-filter-line" /></th>
						<td>Filter</td>
						<td><code>{{ '<' }}span class="icon i-ri-filter-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-filter-off-line" /></th>
						<td>Filter zurücksetzen</td>
						<td><code>{{ '<' }}span class="icon i-ri-filter-off-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-search-line" /></th>
						<td>Suche</td>
						<td><code>{{ '<' }}span class="icon i-ri-search-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-checkbox-circle-line" /></th>
						<td>Radio Option: true</td>
						<td><code>{{ '<' }}span class="icon i-ri-checkbox-circle-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-checkbox-blank-circle-line" /></th>
						<td>Radio Option: false</td>
						<td><code>{{ '<' }}span class="icon i-ri-checkbox-circle-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-check-line" /></th>
						<td>Checkbox: headless + true</td>
						<td><code>{{ '<' }}span class="icon i-ri-check-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-close-line" /></th>
						<td>Checkbox: headless + disabled + false</td>
						<td><code>{{ '<' }}span class="icon i-ri-close-line"{{ ' />' }}</code></td>
					</tr>
					<tr>
						<th><span class="icon i-ri-bar-chart-2-line" /></th>
						<td>Tooltip Label für statistikrelevante Inputs</td>
						<td><code>{{ '<' }}span class="icon i-ri-bar-chart-2-line"{{ ' />' }}</code></td>
					</tr>
				</tbody>
			</table>
		</Variant>
	</Story>
</template>

<script setup lang="ts">

	const iconSizes = {
		'xxs': '0.5rem',
		'xs': '0.75rem',
		'sm': '1rem',
		'default': '1.2rem',
		'lg': '1.5rem',
		'xl': '2rem',
		'xxl': '4rem',
	}

	const iconColors = ['ui', 'ui-brand', 'ui-statistic', 'ui-danger', 'ui-success', 'ui-warning', 'ui-caution'];

</script>
