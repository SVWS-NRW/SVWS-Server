<template>
	<div role="row" class="data-table__tr data-table__thead__tr" :class="{ 'border border-error': schiene_hat_kollisionen }">
		<div role="cell" class="data-table__td" :class="{ 'text-error': schiene_hat_kollisionen }">
			<div class="flex flex-col py-1">
				<span class="font-bold inline-flex items-center gap-1">
					<svws-ui-tooltip v-if="schiene_hat_kollisionen">
						<i-ri-alert-line />
						<template #content>
							<span>Kollision in dieser Schiene</span>
						</template>
					</svws-ui-tooltip>
					{{ schiene_g?.bezeichnung }}
				</span>
				<span class="text-sm">{{ schiene.kurse.size() }} Kurse</span>
				<span class="text-sm">{{ anzahl_schueler }} Schüler</span>
			</div>
		</div>
		<s-kurs-schueler-schiene-kurs v-for="kurs of getSchieneKurse" :key="kurs.hashCode()" :kurs="kurs" :schueler="selected"
			:get-datenmanager="getDatenmanager" :get-ergebnismanager="getErgebnismanager"
			:api-status="apiStatus" :allow-regeln="allowRegeln" :add-regel="addRegel" :remove-regel="removeRegel"
			:update-kurs-schueler-zuordnung="updateKursSchuelerZuordnung" :drag-and-drop-data="dragAndDropData" @dnd="emit('dnd', $event)" />
		<template v-if="maxKurse">
			<div v-for="n in (maxKurse - getSchieneKurse.size())" :key="n" role="cell" class="data-table__td" />
		</template>
	</div>
</template>

<script setup lang="ts">

	import type { GostBlockungRegel, GostBlockungSchiene, GostBlockungsdatenManager, GostBlockungsergebnisKurs, GostBlockungsergebnisManager,
		GostBlockungsergebnisSchiene, SchuelerListeEintrag, List } from "@svws-nrw/svws-core";
	import type { ComputedRef } from "vue";
	import { computed } from "vue";
	import type { ApiStatus } from "~/components/ApiStatus";
	import {get} from "@vueuse/core";

	type DndData = { id: number, fachID: number, kursart: number };

	const props = defineProps<{
		addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
		removeRegel: (id: number) => Promise<GostBlockungRegel | undefined>;
		updateKursSchuelerZuordnung: (idSchueler: number, idKursNeu: number, idKursAlt: number) => Promise<boolean>;
		getDatenmanager: () => GostBlockungsdatenManager;
		getErgebnismanager: () => GostBlockungsergebnisManager;
		schiene: GostBlockungsergebnisSchiene;
		selected: SchuelerListeEintrag;
		apiStatus: ApiStatus;
		allowRegeln: boolean;
		dragAndDropData?: DndData;
		maxKurse?: number;
	}>();

	const emit = defineEmits<{
		(e: 'dnd', data: DndData | undefined): void;
	}>();

	const anzahl_schueler: ComputedRef<number> = computed(() => props.getErgebnismanager().getOfSchieneAnzahlSchueler(props.schiene.id));

	const schiene_g: ComputedRef<GostBlockungSchiene | undefined> = computed(() => props.getErgebnismanager().getSchieneG(props.schiene.id))

	const schiene_hat_kollisionen: ComputedRef<boolean> = computed(() => {
		return props.getErgebnismanager().getOfSchieneSchuelermengeMitKollisionen(props.schiene.id).contains(props.selected.id);
	});

	const getSchieneKurse: ComputedRef<List<GostBlockungsergebnisKurs>> = computed(()=> props.schiene.kurse)

</script>
