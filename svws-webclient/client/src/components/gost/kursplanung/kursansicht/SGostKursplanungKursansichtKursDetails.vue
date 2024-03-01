<template>
	<div class="svws-ui-tr !grid-cols-1 -mt-px border-y border-black/10 !border-t-black/10 shadow-inner select-none" :style="{ '--background-color': bgColor }">
		<div class="pr-3 pl-7 pt-3 pb-4 flex gap-16">
			<div class="flex flex-col gap-2 my-auto">
				<div class="flex items-center gap-4">
					<span class="text-sm font-bold">Kurs:</span>
					<svws-ui-button type="icon" @click="removeKurse([kurs.id])" title="Kurs entfernen" class="ml-1" :disabled="apiStatus.pending"><i-ri-delete-bin-line /></svws-ui-button>
					<svws-ui-button type="secondary" @click="addKurs(kurs.fach_id, kurs.kursart)" title="Kurs hinzufügen" :disabled="apiStatus.pending">Hinzufügen</svws-ui-button>
					<svws-ui-button type="secondary" @click="splitKurs(kurs)" :disabled="apiStatus.pending">Aufteilen</svws-ui-button>
					<template v-if="andereKurse.size > 0">
						<svws-ui-select :model-value="undefined" @update:model-value="kurs2 => (kurs2 !== undefined && kurs2 !== null) && combineKurs(kurs, kurs2)"
							title="Zusammenlegen mit" class="text-sm" headless :items="andereKurse" :item-text="i => getDatenmanager().kursGetName(i.id)" :disabled="apiStatus.pending" />
					</template>
					<span class="text-sm font-bold">Externe Schüler:</span>
					<svws-ui-input-number placeholder="externe Schüler" :model-value="getErgebnismanager().getOfKursAnzahlSchuelerDummy(kurs.id)" @update:model-value="updateExterne" :min="1" headless :disabled="apiStatus.pending" />
				</div>
			</div>
			<s-gost-kursplanung-kursansicht-modal-zusatzkraefte :kurs="kurs" :map-lehrer="mapLehrer" :get-datenmanager="getDatenmanager"
				:add-lehrer-regel="addLehrerRegel" :add-kurs-lehrer="addKursLehrer" :remove-kurs-lehrer="removeKursLehrer" />
			<div class="flex flex-col gap-1 my-auto">
				<div class="flex items-center gap-4">
					<span class="text-sm font-bold">Schienen:</span>
					<div class="flex gap-1">
						<svws-ui-button type="icon" @click="removeSchieneKurs(kurs)" size="small" :disabled="(kurs.anzahlSchienen <= 1) || (apiStatus.pending)"><i-ri-subtract-line /></svws-ui-button>
						<div class="mx-1">{{ kurs.anzahlSchienen }}</div>
						<svws-ui-button type="icon" @click="addSchieneKurs(kurs)" size="small" :disabled="apiStatus.pending"><i-ri-add-line /></svws-ui-button>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed } from 'vue';
	import type { GostBlockungKurs, GostBlockungKursLehrer, GostBlockungRegelUpdate, GostBlockungsdatenManager, GostBlockungsergebnisKurs, GostBlockungsergebnisManager, LehrerListeEintrag } from "@core";
	import type { ApiStatus } from '~/components/ApiStatus';

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		getErgebnismanager: () => GostBlockungsergebnisManager;
		addSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
		regelnUpdate: (update: GostBlockungRegelUpdate) => Promise<void>;
		addLehrerRegel: () => Promise<void>;
		removeSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
		addKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
		removeKurse: (ids: Iterable<number>) => Promise<void>;
		combineKurs: (kurs1 : GostBlockungKurs, fach2: GostBlockungKurs | GostBlockungsergebnisKurs | undefined | null) => Promise<void>;
		splitKurs: (kurs: GostBlockungKurs) => Promise<void>;
		addKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<GostBlockungKursLehrer | undefined>;
		removeKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<void>;
		anzahlSpalten: number;
		kurs: GostBlockungKurs;
		fachart: number;
		mapLehrer: Map<number, LehrerListeEintrag>;
		bgColor: string;
		apiStatus: ApiStatus;
	}>();

	const andereKurse = computed<Map<number, GostBlockungsergebnisKurs>>(() => {
		const result = new Map<number, GostBlockungsergebnisKurs>();
		for (const k of props.getErgebnismanager().getOfFachartKursmenge(props.fachart))
			if (k.id !== props.kurs.id)
				result.set(k.id, k);
		return result;
	});

	async function updateExterne(anzahl: number | null) {
		if (props.apiStatus.pending)
			return;
		const curr = props.getErgebnismanager().getOfKursAnzahlSchuelerDummy(props.kurs.id);
		if (curr === anzahl || (anzahl === null) || anzahl < 0)
			return;
		const update = props.getErgebnismanager().regelupdateCreate_09_KURS_MIT_DUMMY_SUS_AUFFUELLEN(props.kurs.id, anzahl);
		await props.regelnUpdate(update);
	}

</script>

<style lang="postcss">
	.table--row-kursdetail {
		@apply relative z-10 border-b border-black/25;
		/*box-shadow: inset 0 -4px 5px 0 rgba(0, 0, 0, 0.1);*/
    @apply shadow-inner shadow-black/10;

		.data-table__contrast-border & {
			@apply border-black/50;
		}

    .button--secondary {
      @apply hover:text-black hover:border-black hover:bg-black/10;
    }

    .text-input-component {
      @apply text-sm;
    }
	}
</style>
