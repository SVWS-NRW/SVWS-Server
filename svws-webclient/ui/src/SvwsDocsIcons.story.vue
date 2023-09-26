<template>
	<Story title="Icons" id="icons" group="top" icon="ri:landscape-line" :responsive-disabled="true" :layout="{type: 'grid', width: '45%'}">
		<Variant title="Info zu den Icons" source=" " id="info">
			<div class="htw-prose dark:htw-prose-invert">
				<p>
					Bevor ein neues Icon aus dem Icon-Set gesucht wird, sollte immer geprüft werden, ob es bereits ein passendes Icon gibt. Für den Client und Components werden ausschließlich Icons von <a href="https://remixicon.com/" target="_blank">Remix Icon</a> verwendet.
				</p>
				<p>
					In jedem Button Type kann einfach ein Icon neben dem Text optional ergänzt werden. Falls kein Text benötigt wird, ist type="icon" zu verwenden (z.B. im Tabellen-Footer).
				</p>
			</div>
		</Variant>
		<Variant title="Favicon" :source="sourceFavicon">
			<div class="htw-prose dark:htw-prose-invert">
				<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 256 256" width="64" height="64">
					<rect width="256" height="256" fill="#2285D5" rx="51" />
					<path fill="#fff" d="m148.87 22 22.22 74.7h.82L194.13 22H221l-33.52 102h-31.96L122 22h26.87ZM44.38 235 16 137h26.05l14.71 64.22h.81L74.38 137h21.24l16.8 64.36h.82L128 137h26l-28.33 98h-22.72l-17.57-59.43h-.76L67.05 235H44.38ZM212.02 166.15c-.33-3.56-1.8-6.32-4.43-8.3-2.59-2-6.28-3-11.1-3-3.18 0-5.83.4-7.96 1.21-2.12.8-3.71 1.93-4.77 3.35-1.06 1.39-1.61 3-1.64 4.8a7 7 0 0 0 .89 3.93 9.98 9.98 0 0 0 2.99 3.01c1.32.84 2.91 1.59 4.77 2.23a50.5 50.5 0 0 0 6.27 1.7l8.76 1.94a62.74 62.74 0 0 1 13.34 4.27 36.09 36.09 0 0 1 9.75 6.36 24.58 24.58 0 0 1 6.02 8.5A27.9 27.9 0 0 1 237 206.9c-.03 6.28-1.66 11.67-4.88 16.16-3.21 4.5-7.84 7.94-13.88 10.34-6 2.4-13.25 3.59-21.75 3.59-8.52 0-15.95-1.25-22.29-3.74-6.33-2.49-11.26-6.27-14.78-11.35-3.51-5.08-5.32-11.5-5.42-19.27h23.59c.2 3.2 1.07 5.88 2.63 8.01a14.41 14.41 0 0 0 6.42 4.85 25.62 25.62 0 0 0 9.56 1.65c3.31 0 6.13-.43 8.46-1.3a12.3 12.3 0 0 0 5.42-3.65 8.47 8.47 0 0 0 1.94-5.33 7.25 7.25 0 0 0-1.8-4.8c-1.15-1.37-2.94-2.53-5.37-3.5a56.2 56.2 0 0 0-9.15-2.77l-10.65-2.42c-8.82-1.98-15.77-5.16-20.85-9.56-5.07-4.44-7.6-10.42-7.56-17.96-.04-6.14 1.66-11.53 5.07-16.16 3.42-4.62 8.15-8.23 14.18-10.82 6.04-2.59 12.93-3.88 20.66-3.88 7.89 0 14.74 1.31 20.55 3.93 5.83 2.59 10.36 6.23 13.58 10.92 3.22 4.69 4.86 10.12 4.93 16.3h-23.59ZM90.82 51.99c-.35-3.77-1.9-6.7-4.65-8.79-2.7-2.12-6.58-3.19-11.63-3.19-3.34 0-6.12.43-8.34 1.29-2.23.86-3.9 2.04-5 3.54a8.48 8.48 0 0 0-1.73 5.09 7.46 7.46 0 0 0 .94 4.16c.73 1.2 1.77 2.26 3.13 3.19 1.39.89 3.06 1.68 5 2.36 1.95.69 4.14 1.29 6.58 1.8l9.18 2.05a65.32 65.32 0 0 1 13.98 4.53 37.8 37.8 0 0 1 10.22 6.73c2.78 2.6 4.88 5.6 6.3 8.99 1.44 3.39 2.17 7.2 2.2 11.4-.03 6.65-1.74 12.35-5.11 17.11-3.37 4.77-8.22 8.41-14.55 10.95-6.3 2.53-13.9 3.8-22.8 3.8-8.93 0-16.72-1.32-23.36-3.96-6.65-2.63-11.8-6.64-15.5-12.02C32 105.64 30.1 98.84 30 90.62h24.72c.21 3.4 1.13 6.22 2.77 8.48a15.13 15.13 0 0 0 6.73 5.14 26.63 26.63 0 0 0 10.01 1.75c3.48 0 6.43-.47 8.87-1.4 2.47-.92 4.36-2.2 5.68-3.84a9.02 9.02 0 0 0 2.04-5.66 7.72 7.72 0 0 0-1.88-5.08c-1.22-1.44-3.1-2.68-5.63-3.7a58.48 58.48 0 0 0-9.6-2.93L62.55 80.8C53.3 78.72 46 75.35 40.69 70.69c-5.32-4.7-7.96-11.03-7.93-19.01-.03-6.51 1.74-12.21 5.32-17.11 3.59-4.9 8.54-8.72 14.87-11.46S66.49 19 74.6 19c8.27 0 15.45 1.39 21.54 4.16 6.12 2.74 10.86 6.6 14.24 11.56 3.37 4.97 5.09 10.72 5.16 17.27H90.82Z" />
				</svg>
				<p>Ein offizielles Logo für den Client gibt es noch nicht. In der Zwischenzeit wird ein einfaches Favicon verwendet.</p>
			</div>
		</Variant>
		<Variant title="Häufig verwendet" source=" ">
			<table class="svws-docs-icon-table">
				<tr>
					<th><i-ri-add-line /></th>
					<td>Hinzufügen, neuer Datensatz</td>
					<td><code>{{ '<' }}i-ri-add-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-edit-2-line /></th>
					<td>Bearbeiten</td>
					<td><code>{{ '<' }}i-ri-edit-2-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-delete-bin-line /></th>
					<td>Löschen</td>
					<td><code>{{ '<' }}i-ri-delete-bin-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-link /></th>
					<td>Link zu einem anderen Bereich im Client</td>
					<td><code>{{ '<' }}i-ri-link{{ ' />' }}</code></td>
				</tr>
			</table>
			<table class="svws-docs-icon-table">
				<tr>
					<th><i-ri-question-fill /></th>
					<td>Hilfe/Information, Tooltip Trigger</td>
					<td><code>{{ '<' }}i-ri-question-fill{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-information-line /></th>
					<td>Information, Tooltip Trigger</td>
					<td><code>{{ '<' }}i-ri-information-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-alert-line /></th>
					<td>Warnung</td>
					<td><code>{{ '<' }}i-ri-alert-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-error-warning-line /></th>
					<td>Alternative Warnung/Fehler</td>
					<td><code>{{ '<' }}i-ri-error-warning-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-alert-fill /></th>
					<td>Fehler, Steigerung von "Warnung"</td>
					<td><code>{{ '<' }}i-ri-alert-fill{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-checkbox-circle-fill /></th>
					<td>Keine Fehler, gültig</td>
					<td><code>{{ '<' }}i-ri-checkbox-circle-fill{{ ' />' }}</code></td>
				</tr>
			</table>
		</Variant>
		<Variant title="App Icons" source=" ">
			<table class="svws-docs-icon-table">
				<tr>
					<th><i-ri-school-line /></th>
					<td>Schule</td>
					<td><code>{{ '<' }}i-ri-school-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-archive-line /></th>
					<td>Kataloge</td>
					<td><code>{{ '<' }}i-ri-archive-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-group-line /></th>
					<td>Schüler</td>
					<td><code>{{ '<' }}i-ri-group-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-briefcase-line /></th>
					<td>Lehrer</td>
					<td><code>{{ '<' }}i-ri-briefcase-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-team-line /></th>
					<td>Klassen</td>
					<td><code>{{ '<' }}i-ri-team-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-book-2-line /></th>
					<td>Kurse</td>
					<td><code>{{ '<' }}i-ri-book-2-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-graduation-cap-line /></th>
					<td>Oberstufe</td>
					<td><code>{{ '<' }}i-ri-graduation-cap-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-bar-chart-2-line /></th>
					<td>Statistik</td>
					<td><code>{{ '<' }}i-ri-bar-chart-2-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-calendar-event-line /></th>
					<td>Stundenplan</td>
					<td><code>{{ '<' }}i-ri-calendar-event-line{{ ' />' }}</code></td>
				</tr>
			</table>
			<table class="svws-docs-icon-table">
				<tr>
					<th><i-ri-logout-circle-line /></th>
					<td>Logout</td>
					<td><code>{{ '<' }}i-ri-logout-circle-line{{ ' />' }}</code></td>
				</tr>
				<!--<tr>
					<th><i-ri-palette-line /></th>
					<td>Ansicht (Einstellungen)</td>
					<td><code>{{ '<' }}i-ri-palette-line{{ ' />' }}</code></td>
				</tr>-->
			</table>
		</Variant>
		<Variant title="Weitere Icons" source=" ">
			<table class="svws-docs-icon-table">
				<tr>
					<th><i-ri-eye-line /></th>
					<td>Element einblenden</td>
					<td><code>{{ '<' }}i-ri-eye-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-eye-off-line /></th>
					<td>Element ausblenden</td>
					<td><code>{{ '<' }}i-ri-eye-off-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-printer-line /></th>
					<td>Drucken</td>
					<td><code>{{ '<' }}i-ri-printer-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-sparkling-line /></th>
					<td>Automatisierung, z. B. Verteilen von Kursen</td>
					<td><code>{{ '<' }}i-ri-sparkling-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-upload-2-line /></th>
					<td>Importieren, Hochladen</td>
					<td><code>{{ '<' }}i-ri-upload-2-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-download-2-line /></th>
					<td>Exportieren, Herunterladen</td>
					<td><code>{{ '<' }}i-ri-download-2-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-shield-star-line /></th>
					<td>Admin-Rechte, Administrative Benutzergruppe</td>
					<td><code>{{ '<' }}i-ri-shield-star-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-bug-fill /></th>
					<td>Fehlermeldung Bug</td>
					<td><code>{{ '<' }}i-ri-bug-fill{{ ' />' }}</code></td>
				</tr>
			</table>
		</Variant>
		<Variant title="Oberstufe" source=" ">
			<table class="svws-docs-icon-table">
				<tr>
					<th><i-ri-spam-3-line /></th>
					<td>Nichtverteilt</td>
					<td><code>{{ '<' }}i-ri-spam-3-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-error-warning-line /></th>
					<td>Kollision</td>
					<td><code>{{ '<' }}i-ri-error-warning-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-alert-fill /></th>
					<td>Kollision und Nichtverteilt</td>
					<td><code>{{ '<' }}i-ri-alert-fill{{ ' />' }}</code></td>
				</tr>
			</table>
			<table class="svws-docs-icon-table">
				<tr>
					<th><i-ri-draft-line /></th>
					<td>Fachwahl: Schriftlich</td>
					<td><code>{{ '<' }}i-ri-draft-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-speak-line /></th>
					<td>Fachwahl: Mündliche</td>
					<td><code>{{ '<' }}i-ri-speak-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-user-line /></th>
					<td>Überschrift in der Tabelle für Geschlecht</td>
					<td><code>{{ '<' }}i-ri-user-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-settings-3-line /></th>
					<td>Regeln zur Blockung</td>
					<td><code>{{ '<' }}i-ri-settings-3-line{{ ' />' }}</code></td>
				</tr>
			</table>
		</Variant>
		<Variant title="Input Fields und Interaktion" source=" ">
			<table class="svws-docs-icon-table">
				<tr>
					<th><i-ri-draggable /></th>
					<td>Drag & Drop Handle</td>
					<td><code>{{ '<' }}i-ri-draggable{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-expand-up-down-fill /></th>
					<td>Multiselect Input</td>
					<td><code>{{ '<' }}i-ri-expand-up-down-fill{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-filter-line /></th>
					<td>Filter</td>
					<td><code>{{ '<' }}i-ri-filter-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-filter-off-line /></th>
					<td>Filter zurücksetzen</td>
					<td><code>{{ '<' }}i-ri-filter-off-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-search-line /></th>
					<td>Suche</td>
					<td><code>{{ '<' }}i-ri-search-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-checkbox-circle-line /></th>
					<td>Radio Option: true</td>
					<td><code>{{ '<' }}i-ri-checkbox-circle-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-checkbox-blank-circle-line /></th>
					<td>Radio Option: false</td>
					<td><code>{{ '<' }}i-ri-checkbox-circle-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-check-line /></th>
					<td>Checkbox: headless + true</td>
					<td><code>{{ '<' }}i-ri-check-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-close-line /></th>
					<td>Checkbox: headless + disabled + false</td>
					<td><code>{{ '<' }}i-ri-close-line{{ ' />' }}</code></td>
				</tr>
				<tr>
					<th><i-ri-bar-chart2-line /></th>
					<td>Tooltip Label für statistikrelevante Inputs</td>
					<td><code>{{ '<' }}i-ri-bar-chart-2-line{{ ' />' }}</code></td>
				</tr>
			</table>
		</Variant>
	</Story>
</template>

<style lang="postcss" scoped>
.svws-docs-icon-table {
	@apply w-full;

	+ .svws-docs-icon-table {
		@apply mt-8;
	}

	tr {
		@apply grid border-b border-black/10 last:border-0;
		grid-template-columns: 0.25fr 1fr 2fr;

		&:hover {
			@apply text-svws;
		}
	}

	th,
	td {
		@apply py-1 px-2 border-r border-black/10 last:border-0 h-full flex items-center leading-tight;

		svg {
			@apply text-headline-md;
		}
	}

	code {
		@apply select-all bg-black/5 rounded px-1 -my-0.5 dark:bg-white/5;
	}

	th {
		@apply flex items-center justify-center;
	}
}
</style>
<script setup lang="ts">
	const sourceFavicon = `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 256 256">
  <rect width="256" height="256" fill="#2285D5" rx="51"/>
  <path fill="#fff" d="m148.87 22 22.22 74.7h.82L194.13 22H221l-33.52 102h-31.96L122 22h26.87ZM44.38 235 16 137h26.05l14.71 64.22h.81L74.38 137h21.24l16.8 64.36h.82L128 137h26l-28.33 98h-22.72l-17.57-59.43h-.76L67.05 235H44.38ZM212.02 166.15c-.33-3.56-1.8-6.32-4.43-8.3-2.59-2-6.28-3-11.1-3-3.18 0-5.83.4-7.96 1.21-2.12.8-3.71 1.93-4.77 3.35-1.06 1.39-1.61 3-1.64 4.8a7 7 0 0 0 .89 3.93 9.98 9.98 0 0 0 2.99 3.01c1.32.84 2.91 1.59 4.77 2.23a50.5 50.5 0 0 0 6.27 1.7l8.76 1.94a62.74 62.74 0 0 1 13.34 4.27 36.09 36.09 0 0 1 9.75 6.36 24.58 24.58 0 0 1 6.02 8.5A27.9 27.9 0 0 1 237 206.9c-.03 6.28-1.66 11.67-4.88 16.16-3.21 4.5-7.84 7.94-13.88 10.34-6 2.4-13.25 3.59-21.75 3.59-8.52 0-15.95-1.25-22.29-3.74-6.33-2.49-11.26-6.27-14.78-11.35-3.51-5.08-5.32-11.5-5.42-19.27h23.59c.2 3.2 1.07 5.88 2.63 8.01a14.41 14.41 0 0 0 6.42 4.85 25.62 25.62 0 0 0 9.56 1.65c3.31 0 6.13-.43 8.46-1.3a12.3 12.3 0 0 0 5.42-3.65 8.47 8.47 0 0 0 1.94-5.33 7.25 7.25 0 0 0-1.8-4.8c-1.15-1.37-2.94-2.53-5.37-3.5a56.2 56.2 0 0 0-9.15-2.77l-10.65-2.42c-8.82-1.98-15.77-5.16-20.85-9.56-5.07-4.44-7.6-10.42-7.56-17.96-.04-6.14 1.66-11.53 5.07-16.16 3.42-4.62 8.15-8.23 14.18-10.82 6.04-2.59 12.93-3.88 20.66-3.88 7.89 0 14.74 1.31 20.55 3.93 5.83 2.59 10.36 6.23 13.58 10.92 3.22 4.69 4.86 10.12 4.93 16.3h-23.59ZM90.82 51.99c-.35-3.77-1.9-6.7-4.65-8.79-2.7-2.12-6.58-3.19-11.63-3.19-3.34 0-6.12.43-8.34 1.29-2.23.86-3.9 2.04-5 3.54a8.48 8.48 0 0 0-1.73 5.09 7.46 7.46 0 0 0 .94 4.16c.73 1.2 1.77 2.26 3.13 3.19 1.39.89 3.06 1.68 5 2.36 1.95.69 4.14 1.29 6.58 1.8l9.18 2.05a65.32 65.32 0 0 1 13.98 4.53 37.8 37.8 0 0 1 10.22 6.73c2.78 2.6 4.88 5.6 6.3 8.99 1.44 3.39 2.17 7.2 2.2 11.4-.03 6.65-1.74 12.35-5.11 17.11-3.37 4.77-8.22 8.41-14.55 10.95-6.3 2.53-13.9 3.8-22.8 3.8-8.93 0-16.72-1.32-23.36-3.96-6.65-2.63-11.8-6.64-15.5-12.02C32 105.64 30.1 98.84 30 90.62h24.72c.21 3.4 1.13 6.22 2.77 8.48a15.13 15.13 0 0 0 6.73 5.14 26.63 26.63 0 0 0 10.01 1.75c3.48 0 6.43-.47 8.87-1.4 2.47-.92 4.36-2.2 5.68-3.84a9.02 9.02 0 0 0 2.04-5.66 7.72 7.72 0 0 0-1.88-5.08c-1.22-1.44-3.1-2.68-5.63-3.7a58.48 58.48 0 0 0-9.6-2.93L62.55 80.8C53.3 78.72 46 75.35 40.69 70.69c-5.32-4.7-7.96-11.03-7.93-19.01-.03-6.51 1.74-12.21 5.32-17.11 3.59-4.9 8.54-8.72 14.87-11.46S66.49 19 74.6 19c8.27 0 15.45 1.39 21.54 4.16 6.12 2.74 10.86 6.6 14.24 11.56 3.37 4.97 5.09 10.72 5.16 17.27H90.82Z"/>
</svg>`
</script>
