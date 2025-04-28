<template>
	<table class="svws-ui-table svws-clickable h-full w-full overflow-hidden" role="table" aria-label="Tabelle">
		<thead class="svws-ui-thead cursor-pointer mb-1" role="rowgroup" aria-label="Tabellenkopf">
			<tr class="svws-ui-tr" role="row" style="grid-template-columns: 1fr 5fr 5fr 3fr 1rem; min-height: auto;">
				<td class="svws-ui-td" role="columnheader">Kürzel</td>
				<td class="svws-ui-td" role="columnheader">Nachname, Vorname</td>
				<td class="svws-ui-td" role="columnheader">Dienst-Email</td>
				<td class="svws-ui-td" role="columnheader">Initialkennwort</td>
			</tr>
		</thead>
		<tbody class="svws-ui-tbody h-full overflow-y-auto" role="rowgroup" aria-label="Tabelleninhalt">
			<template v-for="lehrer of lehrerListe" :key="lehrer">
				<tr class="svws-ui-tr" role="row" style="grid-template-columns: 1fr 5fr 5fr 3fr 1rem; min-height: auto;">
					<td class="svws-ui-td" role="cell">{{ lehrer.kuerzel }}</td>
					<td class="svws-ui-td" role="cell">{{ lehrer.nachname }}, {{ lehrer.vorname }}</td>
					<td class="svws-ui-td" role="cell">
						<svws-ui-tooltip v-if="(lehrer.eMailDienstlich === null) || (lehrer.eMailDienstlich.trim().length === 0)">
							<span class="icon i-ri-alert-line icon-ui-danger" />
							<template #content>
								Eine fehlende dienstliche Email-Adresse ist für den Web-Noten-Manager nicht zulässig. Bitte tragen Sie diese im Lehrerbereich ein
							</template>
						</svws-ui-tooltip>
						<svws-ui-tooltip v-else-if="!validatorEmail(lehrer.eMailDienstlich)">
							<span class="icon i-ri-alert-line icon-ui-danger" />
							<template #content>
								Die dienstliche Email-Adresse ist fehlerhaft. Korrigieren Sie diese im Lehrerbereich
							</template>
						</svws-ui-tooltip>
						<svws-ui-tooltip v-else-if="emailDuplikate.has(lehrer.eMailDienstlich)">
							<span class="icon i-ri-alert-line icon-ui-danger" />
							<template #content>
								Diese dienstliche Email-Adresse ist bei mehreren Lehrern eingetragen. Dies ist für den Web-Noten-Manager nicht zulässig.
							</template>
						</svws-ui-tooltip>
						<span>{{ lehrer.eMailDienstlich }}</span>
					</td>
					<td class="svws-ui-td" role="cell">
						{{ mapEnmInitialKennwoerter().get(lehrer.id) }}
						<div @click="copyToClipboard(lehrer.id)" class="cursor-pointer place-items-center">
							<span class="icon-sm i-ri-file-copy-line" />
						</div>
					</td>
				</tr>
			</template>
		</tbody>
	</table>
</template>

<script setup lang="ts">

	import { computed } from 'vue';
	import type { EnmLehrerCredentialsProps } from './EnmLehrerCredentialsProps';
	import { ArrayList, DeveloperNotificationException, type ENMLehrer, type List } from "@core";

	const props = defineProps<EnmLehrerCredentialsProps>();

	const validatorEmail = (value: string | null) : boolean => ((value === null) || (value === '')) ? true : (
		/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))[^@]?$/.test(value) ||
		/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(value)
	);

	const lehrerListe = computed<List<ENMLehrer>>(() => {
		const result = new ArrayList<ENMLehrer>(props.enmDaten().lehrer);
		result.sort({ compare: (a : ENMLehrer, b : ENMLehrer) : number => {
			if ((a.nachname !== null) && (b.nachname !== null)) {
				let tmp = a.nachname.localeCompare(b.nachname);
				if (tmp !== 0)
					return tmp;
				if ((a.vorname !== null) && (b.vorname !== null)) {
					tmp = a.vorname.localeCompare(b.vorname);
					if (tmp !== 0)
						return tmp;
					return a.id - b.id;
				}
				if (a.vorname === null)
					return -1;
				if (b.vorname === null)
					return 1;
				return a.id - b.id;
			}
			if (a.nachname === null)
				return -1;
			if (b.nachname === null)
				return 1;
			return a.id - b.id;
		}});
		return result;
	});

	const emailDuplikate = computed<Set<string>>(() => {
		const duplikate = new Set<string>();
		const emails = new Set<string>();
		for (const lehrer of lehrerListe.value) {
			if ((lehrer.eMailDienstlich === null) || (lehrer.eMailDienstlich.trim().length === 0))
				continue;
			if (emails.has(lehrer.eMailDienstlich))
				duplikate.add(lehrer.eMailDienstlich);
			else
				emails.add(lehrer.eMailDienstlich);
		}
		return duplikate;
	});

	async function copyToClipboard(idLehrer: number) {
		try {
			const kennwort = props.mapEnmInitialKennwoerter().get(idLehrer);
			if (kennwort === null)
				throw new DeveloperNotificationException("Initial-Kennwort ist nicht vorhanden und kann daher nicht in die Zwischenablage kopiert werden.");
			else
				await navigator.clipboard.writeText(kennwort);
		} catch(e) {
			throw new DeveloperNotificationException("Initial-Kennwort konnte nicht in die Zwischenablage kopiert werden.");
		}
	}

</script>
