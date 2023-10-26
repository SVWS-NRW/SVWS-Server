<template>
	<div class="svws-ui-tr !grid-cols-1 -mt-px border-y border-black/10 !border-t-black/10 shadow-inner" :style="{ '--background-color': bgColor }">
		<div class="pr-3 pl-7 pt-3 pb-4 flex gap-16">
			<div class="flex flex-col gap-2 my-auto">
				<div class="flex items-center gap-4">
					<span class="text-sm font-bold">Kurs:</span>
					<svws-ui-button type="icon" @click="del_kurs" title="Kurs entfernen" class="ml-1"><i-ri-delete-bin-line /></svws-ui-button>
					<svws-ui-button type="secondary" @click="add_kurs" title="Kurs hinzufügen">Hinzufügen</svws-ui-button>
					<svws-ui-button type="secondary" @click="splitKurs(kurs)">Aufteilen</svws-ui-button>
					<template v-if="andereKurse.size > 0">
						<svws-ui-select :model-value="undefined" @update:model-value="kurs2 => (kurs2 !== undefined && kurs2 !== null) && combineKurs(kurs, kurs2)"
							title="Zusammenlegen mit" class="text-sm" headless :items="andereKurse" :item-text="item => get_kursbezeichnung(item.id)" />
					</template>
				</div>
			</div>
			<s-gost-kursplanung-kursansicht-modal-zusatzkraefte :kurs="kurs" :map-lehrer="mapLehrer" :get-datenmanager="getDatenmanager"
				:add-regel="addRegel" :add-kurs-lehrer="addKursLehrer" :remove-kurs-lehrer="removeKursLehrer" />
			<div class="flex flex-col gap-1 my-auto">
				<div class="flex items-center gap-4">
					<span class="text-sm font-bold">Schienen:</span>
					<div class="flex gap-1">
						<svws-ui-button type="icon" @click="del_schiene" size="small" :disabled="kurs.anzahlSchienen <= 1"><i-ri-subtract-line /></svws-ui-button>
						<div class="mx-1">{{ kurs.anzahlSchienen }}</div>
						<svws-ui-button type="icon" @click="add_schiene" size="small"><i-ri-add-line /></svws-ui-button>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { GostBlockungKurs, GostBlockungKursLehrer, GostBlockungRegel, GostBlockungsdatenManager, GostBlockungsergebnisKurs, GostBlockungsergebnisManager, LehrerListeEintrag } from "@core";
	import type { ComputedRef } from 'vue';
	import { computed } from 'vue';

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		getErgebnismanager: () => GostBlockungsergebnisManager;
		addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
		addSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
		removeSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
		addKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
		removeKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
		combineKurs: (kurs1 : GostBlockungKurs, fach2: GostBlockungKurs | GostBlockungsergebnisKurs | undefined | null) => Promise<void>;
		splitKurs: (kurs: GostBlockungKurs) => Promise<void>;
		addKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<GostBlockungKursLehrer | undefined>;
		removeKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<void>;
		anzahlSpalten: number;
		kurs: GostBlockungKurs;
		fachart: number;
		mapLehrer: Map<number, LehrerListeEintrag>;
		bgColor: string;
	}>();

	const andereKurse: ComputedRef<Map<number, GostBlockungsergebnisKurs>> = computed(() => {
		const result = new Map<number, GostBlockungsergebnisKurs>();
		for (const k of props.getErgebnismanager().getOfFachartKursmenge(props.fachart))
			if (k.id !== props.kurs.id)
				result.set(k.id, k);
		return result;
	});

	function get_kursbezeichnung(kurs_id: number): string {
		return props.getDatenmanager().kursGetName(kurs_id);
	}

	async function add_kurs() {
		await props.addKurs(props.kurs.fach_id, props.kurs.kursart)
	}

	async function del_kurs() {
		await props.removeKurs(props.kurs.fach_id, props.kurs.kursart);
	}

	async function add_schiene() {
		await props.addSchieneKurs(props.kurs);
	}

	async function del_schiene() {
		await props.removeSchieneKurs(props.kurs);
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
