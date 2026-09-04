<template>
	<div class="page page-flex-row">
		<div class="h-full flex flex-col gap-4">
			<div class="flex flex-col gap-8">
				<div class="text-headline-md">{{ `Benutzertyp ${BenutzerTyp.getByID(getBenutzerManager().daten().typ)?.bezeichnung}` }}</div>
				<div class="grid grid-cols-2 gap-2">
					<svws-ui-text-input class="contentFocusField" :model-value="getBenutzerManager().getAnmeldename()" @change="setAnmeldename" :readonly placeholder="Benutzername" />
					<svws-ui-text-input :model-value="getBenutzerManager().getAnzeigename()" @change="setAnzeigename" placeholder="Anzeigename" :readonly="getBenutzerManager().daten().typ !== BenutzerTyp.ALLGEMEIN.id" />
					<svws-ui-text-input v-model.trim="kennwort1" type="password" placeholder="Neues Passwort" />
					<svws-ui-text-input v-model.trim="kennwort2" type="password" placeholder="Neues Passwort wiederholen" />
					<div>
						<svws-ui-button :disabled="!kennwort1 || !kennwort2" @click="setPwd()"> Passwort speichern </svws-ui-button>
					</div>
				</div>
			</div>
			<svws-ui-table scroll selectable :items="listBenutzergruppen" :disable-footer="true" :columns>
				<template #header>
					<tr class="svws-ui-tr gruppen-tr">
						<th id="zugewieseneBenutzergruppen" class="svws-ui-td col-span-2">
							Zugewiesene Benutzergruppen
						</th>
					</tr>
				</template>
				<template #body>
					<tr class="svws-ui-tr gruppen-tr" v-for="row in listBenutzergruppen" :key="row.id">
						<td class="svws-ui-td svws-align-center">
							<svws-ui-checkbox type="toggle"
								:model-value="getBenutzerManager().istInGruppe(row.id)"
								@update:model-value="val => val ? addBenutzerToBenutzergruppe(row.id) : removeBenutzerFromBenutzergruppe(row.id)"
								:disabled="readonly" />
						</td>
						<td class="svws-ui-td">
							<div class="flex items-center gap-0.5">
								<svws-ui-button type="icon" @click="gotoBenutzergruppe(row.id)">
									<span class="icon i-ri-link" />
								</svws-ui-button>
								{{ row.bezeichnung }}
							</div>
						</td>
						<td class="svws-ui-td">
							<svws-ui-tooltip v-if="row.istAdmin">
								<span class="icon i-ri-shield-star-line h-5 w-5 -m-0.5" />
								<template #content>Administrative Gruppe</template>
							</svws-ui-tooltip>
						</td>
					</tr>
				</template>
			</svws-ui-table>
		</div>
		<div class="h-full flex flex-col gap-4">
			<div class="text-headline-md">Einstellungen</div>
			<div v-if="getBenutzerManager().istInAdminGruppe()">
				<span class="inline-flex gap-1 leading-none">
					<span class="icon i-ri-shield-star-line shrink-0 -mt-0.5" />
					<span>Administrator mit allen Kompetenzen</span>
				</span>
			</div>
			<div v-else>
				<svws-ui-checkbox type="toggle" v-model="inputIstAdmin">
					Alle Kompetenzen freigeben
				</svws-ui-checkbox>
			</div>
			<svws-ui-table :items="kompetenzgruppen" scroll>
				<template #header>
					<tr class="svws-ui-tr kompetenz-tr">
						<th id="kompetenz" class="svws-ui-td" :class="{'col-span-2': getBenutzerManager().istAdmin()}">Kompetenz</th>
						<th id="info" class="svws-ui-td">
							<span class="icon cursor-pointer" :class="{ 'i-ri-question-line': !showInfo, 'i-ri-question-fill': showInfo }" @click="toggleShowInfo" />
						</th>
						<th v-if="!getBenutzerManager().istAdmin()" id="uerbenommenGruppe" class="svws-ui-td pl-1! text-ui-50">Übernommen aus der Gruppe</th>
					</tr>
				</template>
				<template #body>
					<template v-for="kompetenzgruppe in kompetenzgruppen" :key="kompetenzgruppe.daten.id">
						<s-benutzer-kompetenzgruppe :kompetenzgruppe :show-info :get-benutzer-manager :add-kompetenz :remove-kompetenz :add-benutzer-kompetenz-gruppe :remove-benutzer-kompetenz-gruppe :benutzer-kompetenzen />
					</template>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, shallowRef } from 'vue';
	import type { BenutzerProps } from './SBenutzerProps';
	import { BenutzerKompetenzGruppe } from '@core/core/types/benutzer/BenutzerKompetenzGruppe';
	import { useBenutzerState } from '@ui/states/BenutzerState';
	import { useSchuleState } from '@ui/states/SchuleState';
	import { BenutzerTyp } from '@core/core/types/benutzer/BenutzerTyp';

	const props = defineProps<BenutzerProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();

	const readonly = computed<boolean>(() => (props.getBenutzerManager().daten().id === benutzerState.benutzerdaten.id));

	const columns = [
		{ key: 'bezeichnung', label: 'Bezeichnung' },
		{ key: 'istAdmin', label: 'ist Admin' },
	];
	const kennwort1 = ref();
	const kennwort2 = ref();

	async function setPwd() {
		if (kennwort1.value === kennwort2.value) {
			await props.setPassword(kennwort1.value);
		} else {
			alert("Kennwörter stimmen nicht überein");
		}
	}

	const kompetenzgruppen = computed<BenutzerKompetenzGruppe[]>(() =>
		BenutzerKompetenzGruppe.values()
			.filter(gr => gr.daten.id >= 0)
			// BenutzerKompetenzGruppe ABSCHLUSS_BK darf nur bei berufsbildenden Schulformen auswählbar sein
			.filter(gr => (gr !== BenutzerKompetenzGruppe.ABSCHLUSS_BK) || schuleState.schulform.istBerufsbildend())
	);

	const inputIstAdmin = computed<boolean>({
		get: () => props.getBenutzerManager().istAdmin(),
		set: (value) => {
			if (value === props.getBenutzerManager().istAdmin()) {
				return;
			}
			void props.setIstAdmin(value);
		},
	});

	const showInfo = shallowRef<boolean>(false);
	function toggleShowInfo() {
		showInfo.value = !showInfo.value;
	}

</script>
<style scoped>
	.gruppen-tr {
		grid-template-columns: minmax(3rem, 0.5fr) minmax(4rem, 10fr) var(--checkbox-width) !important;
	}
	.kompetenz-tr {
		grid-template-columns: minmax(4rem, 2fr) 0.15fr minmax(4rem, 1fr);
	}
</style>
