<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-12 lg:gap-16">
			<svws-ui-content-card :title="`Benutzertyp ${BenutzerTyp.getByID(getBenutzerManager().daten().typ)?.bezeichnung}`">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input class="contentFocusField" :model-value="getBenutzerManager().getAnmeldename()" @change="setAnmeldename" placeholder="Benutzername" />
					<svws-ui-text-input :model-value="getBenutzerManager().getAnzeigename()" @change="setAnzeigename" placeholder="Anzeigename" :readonly="getBenutzerManager().daten().typ !== BenutzerTyp.ALLGEMEIN.id" />
					<svws-ui-text-input v-model.trim="kennwort1" type="password" placeholder="Neues Passwort" />
					<svws-ui-text-input v-model.trim="kennwort2" type="password" placeholder="Neues Passwort wiederholen" />
					<div>
						<svws-ui-button :disabled="!kennwort1 || !kennwort2" @click="setPwd()"> Passwort speichern </svws-ui-button>
					</div>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-content-card>
				<svws-ui-table selectable :items="listBenutzergruppen" :disable-footer="true" :columns>
					<template #header>
						<div role="row" class="svws-ui-tr gruppen-tr">
							<!--<div class="svws-ui-td svws-align-center" role="columnheader" aria-label="Alle auswählen">
						<svws-ui-checkbox :model-value="selected" />
					</div>-->
							<div class="svws-ui-td col-span-2" role="columnheader">
								Zugewiesene Benutzergruppen
							</div>
						</div>
					</template>
					<template #body>
						<div role="row" class="svws-ui-tr gruppen-tr" v-for="row in listBenutzergruppen" :key="row.id">
							<div class="svws-ui-td svws-align-center" role="cell">
								<svws-ui-checkbox type="toggle" :model-value="getBenutzerManager().istInGruppe(row.id)" @update:model-value="val => val ? addBenutzerToBenutzergruppe(row.id) : removeBenutzerFromBenutzergruppe(row.id)" />
							</div>
							<div class="svws-ui-td" role="cell">
								<div class="flex items-center gap-0.5">
									<svws-ui-button type="icon" @click="gotoBenutzergruppe(row.id)">
										<span class="icon i-ri-link" />
									</svws-ui-button>
									{{ row.bezeichnung }}
								</div>
							</div>
							<div class="svws-ui-td" role="cell">
								<svws-ui-tooltip v-if="row.istAdmin">
									<span class="icon i-ri-shield-star-line h-5 w-5 -m-0.5" />
									<template #content>Administrative Gruppe</template>
								</svws-ui-tooltip>
							</div>
						</div>
					</template>
				</svws-ui-table>
			</svws-ui-content-card>
		</div>
		<svws-ui-content-card title="Einstellungen">
			<template #actions>
				<template v-if="getBenutzerManager().istInAdminGruppe()">
					<span class="inline-flex gap-1 leading-none">
						<span class="icon i-ri-shield-star-line shrink-0 -mt-0.5" />
						<span>Administrator mit allen Kompetenzen</span>
					</span>
				</template>
				<template v-else>
					<svws-ui-checkbox type="toggle" v-model="inputIstAdmin">
						Alle Kompetenzen freigeben
					</svws-ui-checkbox>
				</template>
			</template>
			<svws-ui-table :items="kompetenzgruppen">
				<template #header>
					<div class="svws-ui-tr kompetenz-tr" role="row">
						<div class="svws-ui-td" role="columnheader" :class="{'col-span-2': getBenutzerManager().istAdmin()}">Kompetenz</div>
						<div class="svws-ui-td" role="columnheader">
							<span class="icon cursor-pointer" :class="{ 'i-ri-question-line': !showInfo, 'i-ri-question-fill': showInfo }" @click="toggleShowInfo" />
						</div>
						<div v-if="!getBenutzerManager().istAdmin()" class="svws-ui-td !pl-1 text-ui-contrast-50" role="columnheader">Übernommen aus der Gruppe</div>
					</div>
				</template>
				<template #body>
					<template v-for="kompetenzgruppe in kompetenzgruppen" :key="kompetenzgruppe.daten.id">
						<s-benutzer-kompetenzgruppe :kompetenzgruppe :show-info :get-benutzer-manager :add-kompetenz :remove-kompetenz :add-benutzer-kompetenz-gruppe :remove-benutzer-kompetenz-gruppe :benutzer-kompetenzen />
					</template>
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, shallowRef } from 'vue';
	import type { BenutzerProps } from './SBenutzerProps';
	import { BenutzerKompetenzGruppe, BenutzerTyp } from '@core';

	const props = defineProps<BenutzerProps>();
	const columns = [
		{key: 'bezeichnung', label: 'Bezeichnung'},
		{key: 'istAdmin', label: 'ist Admin'},
	]
	const kennwort1 = ref();
	const kennwort2 = ref();

	function setPwd(){
		if (kennwort1.value === kennwort2.value)
			void props.setPassword(kennwort1.value)
		else
			alert("Kennwörter stimmen nicht überein")
	}

	const kompetenzgruppen = computed<BenutzerKompetenzGruppe[]>(() => BenutzerKompetenzGruppe.values().filter(gr => gr.daten.id >= 0));

	const inputIstAdmin = computed<boolean>({
		get: () => props.getBenutzerManager().istAdmin(),
		set: (value) => {
			if (value === props.getBenutzerManager().istAdmin())
				return;
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
