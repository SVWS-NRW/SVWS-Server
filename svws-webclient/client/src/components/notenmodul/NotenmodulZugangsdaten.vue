<template>
	<div class="flex flex-col w-full h-full overflow-hidden">
		<svws-ui-header>
			<span class="inline-block mr-3">Lehrer-Zugangsdaten verwalten</span>
			<ul v-if="lehrerEmailProbleme !== 0" class="text-base mt-2 text-ui-danger">
				<li v-if="lehrerOhneEmail > 1">{{ lehrerOhneEmail }} fehlende Adressen</li>
				<li v-if="lehrerOhneEmail === 1">{{ lehrerOhneEmail }} fehlende Adresse</li>
				<li v-if="lehrerDoppelteEmail > 1">{{ lehrerDoppelteEmail }} Duplikate</li>
				<li v-if="lehrerDoppelteEmail === 1">{{ lehrerDoppelteEmail }} Duplikat</li>
				<li v-if="lehrerFehlerhafteEmail > 1">{{ lehrerFehlerhafteEmail }} fehlerhafte Adressen</li>
				<li v-if="lehrerFehlerhafteEmail === 1">{{ lehrerFehlerhafteEmail }} fehlerhafte Adresse</li>
			</ul>
		</svws-ui-header>
		<div class="page page-flex-row">
			<table class="svws-ui-table svws-clickable h-full w-full overflow-hidden" role="table" aria-label="Tabelle">
				<thead class="svws-ui-thead cursor-pointer mb-1" aria-label="Tabellenkopf">
					<tr class="svws-ui-tr" role="row" style="grid-template-columns: 1fr 5fr 5fr 3fr 1rem; min-height: auto;">
						<th id="kuerzel" class="svws-ui-td" role="columnheader">Kürzel</th>
						<th id="name" class="svws-ui-td" role="columnheader">Nachname, Vorname</th>
						<th id="mail" class="svws-ui-td" role="columnheader">Dienst-Email</th>
						<th id="kennwort" class="svws-ui-td" role="columnheader">Initialkennwort</th>
					</tr>
				</thead>
				<tbody class="svws-ui-tbody h-full overflow-y-auto" aria-label="Tabelleninhalt">
					<template v-for="lehrer of lehrerListe" :key="lehrer.id">
						<tr class="svws-ui-tr" role="row" style="grid-template-columns: 1fr 5fr 5fr 3fr 1rem; min-height: auto;">
							<td class="svws-ui-td">{{ lehrer.kuerzel }}</td>
							<td class="svws-ui-td">{{ lehrer.nachname }}, {{ lehrer.vorname }}</td>
							<td class="svws-ui-td">
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
							<td class="svws-ui-td">
								{{ mapEnmInitialKennwoerter().get(lehrer.id) }}
								<div @click="copyToClipboard(lehrer.id)" class="cursor-pointer place-items-center">
									<span class="icon-sm i-ri-file-copy-line" />
								</div>
							</td>
						</tr>
					</template>
				</tbody>
			</table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from 'vue';
	import type { NotenmodulZugangsdatenProps } from './NotenmodulZugangsdatenProps';
	import { ArrayList, DeveloperNotificationException, type ENMLehrer, type List } from "@core";

	const props = defineProps<NotenmodulZugangsdatenProps>();

	const validatorEmail = (value: string | null): boolean => ((value === null) || (value === '')) ? true : (
		/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))[^@]?$/.test(value) ||
		/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(value)
	);

	const lehrerListe = computed<List<ENMLehrer>>(() => {
		const result = new ArrayList<ENMLehrer>(props.manager().daten.lehrer);
		result.sort({ compare: (a: ENMLehrer, b: ENMLehrer): number => {
			if ((a.nachname !== null) && (b.nachname !== null)) {
				let tmp = a.nachname.localeCompare(b.nachname);
				if (tmp !== 0) {
					return tmp;
				}
				if ((a.vorname !== null) && (b.vorname !== null)) {
					tmp = a.vorname.localeCompare(b.vorname);
					if (tmp !== 0) {
						return tmp;
					}
					return a.id - b.id;
				}
				if (a.vorname === null) {
					return -1;
				}
				if (b.vorname === null) {
					return 1;
				}
				return a.id - b.id;
			}
			if (a.nachname === null) {
				return -1;
			}
			if (b.nachname === null) {
				return 1;
			}
			return a.id - b.id;
		} });
		return result;
	});

	const emailDuplikate = computed<Set<string>>(() => {
		const duplikate = new Set<string>();
		const emails = new Set<string>();
		for (const lehrer of lehrerListe.value) {
			if ((lehrer.eMailDienstlich === null) || (lehrer.eMailDienstlich.trim().length === 0)) {
				continue;
			}
			if (emails.has(lehrer.eMailDienstlich)) {
				duplikate.add(lehrer.eMailDienstlich);
			} else {
				emails.add(lehrer.eMailDienstlich);
			}
		}
		return duplikate;
	});

	async function copyToClipboard(idLehrer: number) {
		try {
			const kennwort = props.mapEnmInitialKennwoerter().get(idLehrer);
			if (kennwort === null) {
				throw new DeveloperNotificationException("Initial-Kennwort ist nicht vorhanden und kann daher nicht in die Zwischenablage kopiert werden.");
			} else {
				await navigator.clipboard.writeText(kennwort);
			}
		} catch {
			throw new DeveloperNotificationException("Initial-Kennwort konnte nicht in die Zwischenablage kopiert werden.");
		}
	}



	const { lehrerOhneEmail, lehrerDoppelteEmail, lehrerFehlerhafteEmail, lehrerEmailProbleme } = computed(() => {
		const lehrerOhneEmail = ref<number>(0);
		const lehrerDoppelteEmail = ref<number>(0);
		const lehrerFehlerhafteEmail = ref<number>(0);
		const lehrerEmailProbleme = computed<number>(() => lehrerOhneEmail.value + lehrerDoppelteEmail.value + lehrerFehlerhafteEmail.value);
		const enmDaten = props.manager().daten;
		let emailFehlt = 0;
		let emailDoppelt = 0;
		let emailFehlerhaft = 0;
		const adressen = new Set<string>();
		for (const lehrer of enmDaten.lehrer) {
			if ((lehrer.eMailDienstlich === null) || (lehrer.eMailDienstlich.trim().length === 0)) {
				emailFehlt++;
				continue;
			}
			if (adressen.has(lehrer.eMailDienstlich)) {
				emailDoppelt++;
				continue;
			}
			adressen.add(lehrer.eMailDienstlich);
			if (!validatorEmail(lehrer.eMailDienstlich)) {
				emailFehlerhaft++;
			}
		}
		lehrerOhneEmail.value = emailFehlt;
		lehrerDoppelteEmail.value = emailDoppelt;
		lehrerFehlerhafteEmail.value = emailFehlerhaft;
		return { lehrerOhneEmail, lehrerDoppelteEmail, lehrerFehlerhafteEmail, lehrerEmailProbleme };
	}).value;
</script>
