<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" type="default" size="big">
		<template #modalTitle>Aufsichtsbereiche mit Vorlage synchronisieren</template>
		<template #modalContent>
			<div>Zur Synchronisation stehen Einträge, die entweder nur im Stundenplan oder nur in der Vorlage vorhanden sind (grün) oder sich ausgehend vom Kürzel voneinander unterscheiden. Durch eine Übertragung in den jeweils anderen Katalog werden vorhandene Daten überschrieben.</div>
			<div class="flex justify-between gap-2 overflow-hidden">
				<div v-if="setVorlage.size > 0" class="overflow-auto w-full">
					<div class="text-2xl">Vorlage</div>
					<svws-ui-table :items="mapVorlage.entries().filter(([k,_v]) => onlyVorlage.has(k) || intersectionVorlageStundenplan.has(k)).map(([_k,v]) => v)" selectable v-model="selectedVorlage" :columns>
						<template #cell(kuerzel)="{ rowData: r }">
							<span :class="intersectionVorlageStundenplan.has(r.kuerzel) ? 'bg-ui-warning' : 'bg-ui-success-secondary'">{{ r.kuerzel }}</span>
						</template>
						<template #actions>
							<svws-ui-button v-if="selectedVorlage.length > 0" type="secondary" @click="syncToStundenplan"> Ausgewählte in Stundenplan übertragen </svws-ui-button>
						</template>
					</svws-ui-table>
				</div>
				<div v-if="setStundenplan.size > 0" class="overflow-auto w-full">
					<div class="text-2xl">Stundenplan</div>
					<svws-ui-table :items="mapStundenplan.entries().filter(([k,_v]) => onlyStundenplan.has(k) || intersectionVorlageStundenplan.has(k)).map(([_k,v]) => v)" selectable v-model="selectedStundenplan" :columns>
						<template #cell(kuerzel)="{ rowData: r }">
							<span :class="intersectionVorlageStundenplan.has(r.kuerzel) ? 'bg-ui-warning' : 'bg-ui-success-secondary'">{{ r.kuerzel }}</span>
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
	import type { List, StundenplanAufsichtsbereich, StundenplanListeManager } from "@core";

	const props = defineProps<{
		aufsichtsbereicheSyncToVorlage: (aufsichtsbereiche: StundenplanAufsichtsbereich[]) => Promise<void>;
		aufsichtsbereicheSyncToStundenplan: (aufsichtsbereiche: StundenplanAufsichtsbereich[]) => Promise<void>;
		listAufsichtsbereiche: () => List<StundenplanAufsichtsbereich>;
		manager: () => StundenplanListeManager;
	}>();

	const show = ref<boolean>(false);

	const selectedVorlage = ref<StundenplanAufsichtsbereich[]>([]);
	const selectedStundenplan = ref<StundenplanAufsichtsbereich[]>([]);

	const columns = [
		{key: 'kuerzel', label: 'Kürzel', span: 1},
		{key: 'beschreibung', label: 'Beschreibung', span: 2},
	];

	const setVorlage = computed(() => new Set([...props.listAufsichtsbereiche()].map(r => r.kuerzel)));
	const setStundenplan = computed(() => new Set([...props.manager().daten().aufsichtsbereichGetMengeAsList()].map(r => r.kuerzel)));
	const onlyVorlage = computed(() => setVorlage.value.difference(setStundenplan.value));
	const onlyStundenplan = computed(() => setStundenplan.value.difference(setVorlage.value));
	const mapVorlage = computed(() => new Map([...props.listAufsichtsbereiche()].map(r => [r.kuerzel, r])));
	const mapStundenplan = computed(() => new Map([...props.manager().daten().aufsichtsbereichGetMengeAsList()].map(r => [r.kuerzel, r])));
	const intersectionVorlageStundenplan = computed(() => {
		const int = setStundenplan.value.intersection(setVorlage.value);
		const set = new Set<string>();
		for (const i of int) {
			const a = mapStundenplan.value.get(i);
			const b = mapVorlage.value.get(i);
			if ((a !== undefined) && (a.beschreibung !== b?.beschreibung))
				set.add(a.kuerzel);
		}
		return set;
	});


	function openModal() {
		show.value = true;
	}

	async function syncToVorlage() {
		await props.aufsichtsbereicheSyncToVorlage(selectedStundenplan.value);
		selectedStundenplan.value = [];
	}

	async function syncToStundenplan() {
		await props.aufsichtsbereicheSyncToStundenplan(selectedVorlage.value);
		selectedVorlage.value = [];
	}

</script>
