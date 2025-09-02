<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" type="default" size="big">
		<template #modalTitle>Pausenzeiten mit Vorlage synchronisieren</template>
		<template #modalContent>
			<div>Zur Synchronisation stehen Einträge, die entweder nur im Stundenplan oder nur in der Vorlage vorhanden sind. Achtung, Einträge mit Klassen lassen sich aktuell nicht synchronisieren.</div>
			<div class="flex justify-between gap-2 overflow-hidden">
				<div v-if="setVorlage.size > 0" class="overflow-auto w-full">
					<div class="text-2xl">Vorlage</div>
					<svws-ui-table :items="mapVorlage.entries().filter(([k,_v]) => onlyVorlage.has(k)).map(([_k,v]) => v)" selectable v-model="selectedVorlage" :columns>
						<template #cell(wochentag)="{ value }">
							<span>{{ Wochentag.fromIDorException(value).beschreibung }}</span>
						</template>
						<template #cell(beginn)="{ value }">
							<span>{{ DateUtils.getStringOfUhrzeitFromMinuten(value ?? 0) }}</span>
						</template>
						<template #cell(ende)="{ value }">
							<span>{{ DateUtils.getStringOfUhrzeitFromMinuten(value ?? 0) }}</span>
						</template>
						<template #cell(klassen)="{ value }">
							<span>{{ klassenbezeichnungen(value) }}</span>
						</template>
						<template #actions>
							<svws-ui-button v-if="selectedVorlage.length > 0" type="secondary" @click="syncToStundenplan"> Ausgewählte in Stundenplan übertragen </svws-ui-button>
						</template>
					</svws-ui-table>
				</div>
				<div v-if="setStundenplan.size > 0" class="overflow-auto w-full">
					<div class="text-2xl">Stundenplan</div>
					<svws-ui-table :items="mapStundenplan.entries().filter(([k,_v]) => onlyStundenplan.has(k)).map(([_k,v]) => v)" selectable v-model="selectedStundenplan" :columns>
						<template #cell(wochentag)="{ value }">
							<span>{{ Wochentag.fromIDorException(value).beschreibung }}</span>
						</template>
						<template #cell(beginn)="{ value }">
							<span>{{ DateUtils.getStringOfUhrzeitFromMinuten(value ?? 0) }}</span>
						</template>
						<template #cell(ende)="{ value }">
							<span>{{ DateUtils.getStringOfUhrzeitFromMinuten(value ?? 0) }}</span>
						</template>
						<template #cell(klassen)="{ value }">
							<span>{{ klassenbezeichnungen(value) }}</span>
						</template>
						<template #actions>
							<svws-ui-button v-if="selectedStundenplan.length > 0" type="secondary" @click="syncToVorlage"> Ausgewählte in Vorlage übertragen </svws-ui-button>
						</template>
					</svws-ui-table>
				</div>
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { List, StundenplanPausenzeit } from "@core";
	import type { StundenplanListeManager } from "@ui";
	import { Wochentag, DateUtils } from "@core"

	const props = defineProps<{
		pausenzeitenSyncToVorlage: (raeume: StundenplanPausenzeit[]) => Promise<void>;
		pausenzeitenSyncToStundenplan: (raeume: StundenplanPausenzeit[]) => Promise<void>;
		listPausenzeiten: () => List<StundenplanPausenzeit>;
		manager: () => StundenplanListeManager;
	}>();

	const show = ref<boolean>(false);

	const selectedVorlage = ref<StundenplanPausenzeit[]>([]);
	const selectedStundenplan = ref<StundenplanPausenzeit[]>([]);

	const columns = [
		{key: 'wochentag', label: 'Wochentag', span: 1},
		{key: 'beginn', label: 'Beginn', span: 1},
		{key: 'ende', label: 'Ende', span: 1},
		{key: 'klassen', label: 'Klassen', span: 2},
	];

	const setVorlage = computed(() => new Set([...props.listPausenzeiten()].map(r => JSON.stringify(r))));
	const setStundenplan = computed(() => new Set([...props.manager().daten().pausenzeitGetMengeAsList()].map(r => JSON.stringify(r))));
	const onlyVorlage = computed(() => setVorlage.value.difference(setStundenplan.value));
	const onlyStundenplan = computed(() => setStundenplan.value.difference(setVorlage.value));
	const mapVorlage = computed(() => new Map([...props.listPausenzeiten()].map(r => [JSON.stringify(r), r])));
	const mapStundenplan = computed(() => new Map([...props.manager().daten().pausenzeitGetMengeAsList()].map(r => [JSON.stringify(r), r])));


	function openModal() {
		show.value = true;
	}

	async function syncToVorlage() {
		await props.pausenzeitenSyncToVorlage(selectedStundenplan.value);
		selectedStundenplan.value = [];
	}

	async function syncToStundenplan() {
		await props.pausenzeitenSyncToStundenplan(selectedVorlage.value);
		selectedVorlage.value = [];
	}

	function klassenbezeichnungen(klassen: number[]) {
		const str = []
		if (klassen.length === 0)
			return "";
		for (const k of klassen)
			str.push(props.manager().daten().klasseGetByIdOrException(k).kuerzel);
		return str.join(', ');
	}

</script>
