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
			<div v-if="!manager().daten.lehrer.isEmpty()" class="w-64">
				<svws-ui-text-input v-model="search" type="search" placeholder="Suchen" />
			</div>
		</svws-ui-header>
		<div class="page ">
			<div v-if="gridManager.daten.isEmpty()">
				Es sind keine Lehrkräfte vorhanden, die hier angezeigt werden können.
			</div>
			<div v-else class="h-full overflow-auto flex gap-16">
				<ui-table-grid name="Lehrer" :manager="() => gridManager">
					<template #header>
						<template v-for="col of gridManager.cols.values()" :key="col.name">
							<th v-if="col.kuerzel === 'Auswahl'" class="flex items-center justify-center">
								<svws-ui-checkbox :model-value="(auswahl.length === gridManager.daten.size()) && (auswahl.length > 0)"
									:indeterminate="(auswahl.length > 0) && (auswahl.length < gridManager.daten.size())"
									@update:model-value="value => auswahl = value ? [...gridManager.daten] : []" />
							</th>
							<th v-else class="flex justify-center" :class="[col.kuerzel === '2FA' ? 'text-center' : 'text-left']">
								{{ col.kuerzel }}
							</th>
						</template>
					</template>
					<template #default="{ row: lehrer }">
						<td class="flex items-center justify-center">
							<svws-ui-checkbox :model-value="auswahl.includes(lehrer)" @update:model-value="toggleSelection(lehrer)" />
						</td>
						<td @click="open(lehrer.id)" class="flex flex-row items-center justify-start gap-2">
							<span class="cursor-pointer icon-sm i-ri-link" title="Zum Lehrerbereich wechseln" />
							{{ lehrer.kuerzel }}
						</td>
						<td class="text-left flex justify-center">
							{{ lehrer.nachname }}, {{ lehrer.vorname }}
						</td>
						<td class="flex items-center flex-row justify-start gap-2">
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
							<div v-if="lehrer.eMailDienstlich !== null" @click="copyToClipboard(lehrer, 'mail')" class="cursor-pointer flex items-center gap-2">
								<span>{{ lehrer.eMailDienstlich }}</span>
								<span class="icon-sm i-ri-file-copy-line" :class="{ 'ping-normal': ((ping?.id === lehrer.id) && (pingType === 'mail')) }" />
							</div>
							<div v-else @click="open(lehrer.id)">
								<span>fehlt</span>
							</div>
						</td>
						<td class="text-left flex justify-center">
							<div v-if="mapEnmInitialKennwoerter().get(lehrer.id) !== null" @click="copyToClipboard(lehrer, 'kennwort')" class="cursor-pointer flex gap-2 items-center">
								<span class="font-mono tracking-wider select-all">
									{{ mapEnmInitialKennwoerter().get(lehrer.id) }}
								</span>
								<span v-if="lehrer.istInitialPassword" class="icon-sm i-ri-file-copy-line" :class="{ 'ping-normal': ((ping?.id === lehrer.id) && (pingType === 'kennwort')) }" />
								<svws-ui-tooltip v-else>
									<span class="icon-sm i-ri-error-warning-line icon-ui-caution" />
									<template #content>
										Eine erste Anmeldung vom Benutzer am Wenom hat stattgefunden und es wurde ein neues Passwort generiert. Dies wird hier nicht angezeigt.
									</template>
								</svws-ui-tooltip>
							</div>
							<div v-else>kein Kennwort gesetzt</div>
						</td>
						<td>
							<div class="flex items-center justify-center h-full">
								<svws-ui-tooltip>
									<span v-if="lehrer.art2FA > 0" class="icon-sm i-ri-verified-badge-fill icon-ui-success" />
									<span v-else class="icon-sm i-ri-alert-fill icon-ui-danger" />
									<template #content>
										<span v-if="lehrer.art2FA > 0">Es wurde eine Zwei-Faktor-Authentifizierung eingerichtet ({{ lehrer.art2FA === 1 ? 'TOTP' : 'EMail' }}).</span>
										<span v-else>Es wurde keine Zwei-Faktor-Authentifizierung eingerichtet.</span>
									</template>
								</svws-ui-tooltip>
							</div>
						</td>
					</template>
				</ui-table-grid>
				<div v-if="(selected !== null) && (auswahl.length === 0)">
					<div class="text-headline-md">{{ selected.nachname }}, {{ selected.vorname }}</div>
					<div class="mb-4">Individuelle Einstellungen für die Zugangsdaten der ausgewählten Lehrkraft</div>
					<div class="content-card mt-4">
						<div class="content-card--header">
							<div class="content-card--headline">Passwort</div>
						</div>
						<div class="content-card--content">
							<div class="">
								Das Passwort wird auf das Initialpasswort zurückgesetzt.
								<br>Bei der nächsten Anmeldung des Benutzers wird ein individuelles Passwort generiert.
								<div class="flex items-center">
									<svws-ui-button type="primary" @click="doResetPassword()">Passwort zurücksetzen</svws-ui-button>
									<span v-if="pingType === 'resetPassword'" class="icon-xl i-ri-check-line icon-ui-success" />
								</div>
							</div>
							<div class="mt-4">
								Ersetze das Initialpasswort mit einem Neuen.
								<div class="flex items-center">
									<svws-ui-button type="primary" @click="doGenerateInitialPassword()">Neues Initialpasswort</svws-ui-button>
									<span v-if="pingType === 'generateInitialPassword'" class="icon-xl i-ri-check-line icon-ui-success" />
								</div>
							</div>
						</div>
					</div>
					<div class="content-card mt-4">
						<div class="content-card--header">
							<div class="content-card--headline">Zwei-Faktor-Authentifizierung</div>
						</div>
						<div class="content-card--content">
							<div>
								Welche Art der Zwei-Faktor-Authentifizierung soll verwendet werden
								<div class="flex gap-2">
									<svws-ui-button @click="art2faAuswahl(0)" :disabled="selected.art2FA === 0">Kein 2FA</svws-ui-button>
									<svws-ui-button @click="art2faAuswahl(1)" :disabled="selected.art2FA === 1">TOTP</svws-ui-button>
								</div>
							</div>
							<div v-if="selected.art2FA === 1" class="mt-4">
								Zwei-Faktor-Authentifizierung (2FA)
								<div class="flex items-center">
									<svws-ui-button type="primary" @click="reset">TOTP Shared Secret zurücksetzen</svws-ui-button>
									<span v-if="pingType === 'resetTotp'" class="icon-xl i-ri-check-line icon-ui-success" />
								</div>
							</div>
						</div>
					</div>
				</div>
				<div v-else-if="auswahl.length > 0">
					<div class="text-headline-md">{{ auswahl.length === 1 ? 'Lehrkraft' : 'Lehrkräfte' }} bearbeiten</div>
					<div class="content-card mt-4">
						<div class="content-card--header">
							<div class="content-card--headline">Passwort</div>
						</div>
						<div class="content-card--content">
							<div>
								Die Passwörter werden auf das jeweilige Initialpasswort zurückgesetzt.
								<br>Bei der nächsten Anmeldung des Benutzers wird ein individuelles Passwort generiert.
								<div class="flex items-center">
									<svws-ui-button type="primary" @click="doResetPassword()">Passwörter zurücksetzen</svws-ui-button>
									<span v-if="pingType === 'resetPassword'" class="icon-xl i-ri-check-line icon-ui-success" />
								</div>
							</div>
							<div class="mt-4">
								Ersetze die Initialpasswörter mit einem Neuen.
								<div class="flex items-center">
									<svws-ui-button type="primary" @click="doGenerateInitialPassword()">Neues Initialpasswort</svws-ui-button>
									<span v-if="pingType === 'generateInitialPassword'" class="icon-xl i-ri-check-line icon-ui-success" />
								</div>
							</div>
						</div>
					</div>
					<div class="content-card mt-4">
						<div class="content-card--header">
							<div class="content-card--headline">Zwei-Faktor-Authentifizierung</div>
						</div>
						<div class="content-card--content">
							<div>
								Welche Art der Zwei-Faktor-Authentifizierung soll verwendet werden
								<div class="flex gap-2">
									<svws-ui-button @click="art2faAuswahl(0)" :disabled="!auswahl.some(l => l.art2FA === 1)">Kein 2FA</svws-ui-button>
									<svws-ui-button @click="art2faAuswahl(1)" :disabled="!auswahl.some(l => l.art2FA === 0)">TOTP</svws-ui-button>
								</div>
							</div>
							<div v-if="auswahl.some(l => l.art2FA === 1)" class="mt-4">
								Zwei-Faktor-Authentifizierung (2FA)
								<div class="flex items-center">
									<svws-ui-button type="primary" @click="reset">TOTP Shared Secrets zurücksetzen</svws-ui-button>
									<span v-if="pingType === 'resetTotp'" class="icon-xl i-ri-check-line icon-ui-success" />
								</div>
							</div>
						</div>
					</div>
				</div>
				<div v-else class="font-bold">
					Wählen Sie zur Bearbeitung eine oder mehrere Lehrkräfte aus der Übersicht aus.
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, triggerRef } from 'vue';
	import type { NotenmodulZugangsdatenProps } from './NotenmodulZugangsdatenProps';
	import { ENMv2Lehrer } from '@core/core/data/enm/v2/ENMv2Lehrer';
	import { DeveloperNotificationException } from '@core/core/exceptions/DeveloperNotificationException';
	import { ArrayList } from '@core/java/util/ArrayList';
	import type { List } from '@core/java/util/List';
	import { GridManager } from '@ui/ui/controls/tablegrid/GridManager';

	const props = defineProps<NotenmodulZugangsdatenProps>();
	const search = ref<string>("");
	const auswahl = ref<ENMv2Lehrer[]>([]);

	function toggleSelection(row: ENMv2Lehrer) {
		const idx = auswahl.value.indexOf(row);
		if (idx === -1) {
			auswahl.value.push(row);
		} else {
			auswahl.value.splice(idx, 1);
		}
	}

	async function art2faAuswahl(type: number) {
		// Wenn keine Mehrfachauswahl vorhanden ist, aber ein Eintrag selektiert wurde, nur dort aktualisieren
		if ((auswahl.value.length === 0) && (selected.value !== null)) {
			await props.set2fa(type, selected.value.id);
			selected.value.art2FA = type;
			triggerRef(selected);
			triggerRef(lehrerListe);
			return;
		}

		// wenn mehrere Lehrer in der Liste ausgewählt sind, diese aktualisieren
		if (auswahl.value.length > 0) {
			for (const lehrer of auswahl.value) {
				if (lehrer.art2FA !== type) {
					await props.set2fa(type, lehrer.id);
				}
				lehrer.art2FA = type;
			}
			triggerRef(lehrerListe);
		}
	}

	const validatorEmail = (value: string | null): boolean => ((value === null) || (value === '')) ? true : (
		/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))[^@]?$/.test(value) ||
		/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(value)
	);

	const selected = computed(() => (gridManager.focusRow === null) ? null : lehrerListe.value.get(gridManager.focusRow));

	const lehrerListe = computed<List<ENMv2Lehrer>>(() => {
		const searchValueLowerCase = search.value.toLocaleLowerCase();
		const result = new ArrayList<ENMv2Lehrer>();
		if (searchValueLowerCase === "") {
			result.addAll(props.manager().daten.lehrer);
		} else {
			for (const e of props.manager().daten.lehrer) {
				if ((e.kuerzel !== null && e.kuerzel.toLocaleLowerCase().includes(searchValueLowerCase))
					|| ((e.nachname !== null) && e.nachname.toLocaleLowerCase().includes(searchValueLowerCase))
					|| ((e.vorname !== null) && e.vorname.toLocaleLowerCase().includes(searchValueLowerCase))) {
					result.add(e);
				}
			}
		}
		result.sort({ compare: (a: ENMv2Lehrer, b: ENMv2Lehrer): number => {
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

	const ping = ref<ENMv2Lehrer | null>(null);
	const pingType = ref<'mail' | 'kennwort' | 'resetPassword' | 'generateInitialPassword' | 'resetTotp' | null>(null);

	function pingTimer(lehrer: ENMv2Lehrer, type: 'mail' | 'kennwort' | 'resetPassword' | 'generateInitialPassword' | 'resetTotp') {
		ping.value = lehrer;
		pingType.value = type;
		setTimeout(() => {
			ping.value = null;
			pingType.value = null;
		}, 3000);
	}

	async function copyToClipboard(lehrer: ENMv2Lehrer, type: 'mail' | 'kennwort') {
		const text = type === 'mail' ? lehrer.eMailDienstlich : props.mapEnmInitialKennwoerter().get(lehrer.id);
		try {
			if (text === null) {
				throw new DeveloperNotificationException("Initial-Kennwort ist nicht vorhanden und kann daher nicht in die Zwischenablage kopiert werden.");
			} else {
				await navigator.clipboard.writeText(text);
				pingTimer(lehrer, type);
			}
		} catch {
			throw new DeveloperNotificationException("Benutzername oder Initial-Kennwort konnte nicht in die Zwischenablage kopiert werden.");
		}
	}

	async function doResetPassword() {
		if (auswahl.value.length > 0) {
			for (const lehrer of auswahl.value) {
				await props.resetPassword(lehrer.id);
				lehrer.istInitialPassword = true;
			}
			pingTimer(new ENMv2Lehrer(), 'resetPassword');
			return;
		}
		if (selected.value === null) {
			return;
		}
		await props.resetPassword(selected.value.id);
		pingTimer(selected.value, "resetPassword");
		selected.value.istInitialPassword = true;
		triggerRef(selected);
	}

	async function doGenerateInitialPassword() {
		if (auswahl.value.length > 0) {
			for (const lehrer of auswahl.value) {
				const newInitialPassword = await props.generateInitialPassword(lehrer.id);
				props.mapEnmInitialKennwoerter().put(lehrer.id, newInitialPassword);
			}
			pingTimer(new ENMv2Lehrer(), 'generateInitialPassword');
			return;
		}
		if (selected.value === null) {
			return;
		}
		const newInitialPassword = await props.generateInitialPassword(selected.value.id);
		props.mapEnmInitialKennwoerter().put(selected.value.id, newInitialPassword);
		pingTimer(selected.value, "generateInitialPassword");
	}

	async function reset() {
		if (auswahl.value.length > 0) {
			for (const lehrer of auswahl.value) {
				await props.resetTotp(lehrer.id);
			}
			pingTimer(new ENMv2Lehrer(), 'resetTotp');
			return;
		}
		if (selected.value === null) {
			return;
		}
		await props.resetTotp(selected.value.id);
		pingTimer(selected.value, "resetTotp");
	}

	const gridManager = new GridManager<string, ENMv2Lehrer, List<ENMv2Lehrer>>({
		daten: computed(() => lehrerListe.value),
		getRowKey: row => `ID_${row.id}`,
		allowEmptyRowSelection: true,
		columns: [
			{ kuerzel: "Auswahl", name: "Auswahl", width: "3rem", hideable: false },
			{ kuerzel: "Kürzel", name: "Kürzel", width: '1fr' },
			{ kuerzel: "Nachname, Vorname", name: "Nachname, Vorname", width: '5fr' },
			{ kuerzel: "Dienst-Email", name: "Dienst-Email", width: '5fr' },
			{ kuerzel: "Initialkennwort", name: "Initialkennwort", width: '3fr' },
			{ kuerzel: "2FA", name: "2FA", width: '1fr' },
		],
	});

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

<style scoped>

	:deep(.svws-ui-checkbox) input[type="checkbox"] {
		margin: 0px !important;
		padding: 0px !important;
	}

	@keyframes ping-normal {
		0% { transform: scale(0.2); opacity: 0.8; }
		80% { transform: scale(1.2); opacity: 0; }
		100% { transform: scale(2.2); opacity: 0; }
	}

	.ping-normal {
		animation: ping-normal 1s ease 0s 1 normal none;
	}

</style>
