<template>
	<div class="table--row-kursdetail" :style="{ 'background-color': bgColor }">
		<div class="pr-3 pl-6 pt-2 pb-5 flex gap-10 items-center">
			<s-gost-kursplanung-kursansicht-modal-zusatzkraefte :kurs="kurs" :map-lehrer="mapLehrer" :get-datenmanager="getDatenmanager"
				:add-regel="addRegel" :add-kurs-lehrer="addKursLehrer" :remove-kurs-lehrer="removeKursLehrer" />
			<div class="flex items-center gap-1">
				<svws-ui-button type="secondary" @click="del_kurs" title="Kurs entfernen">
					Kurs <i-ri-subtract-line />
				</svws-ui-button>
				<svws-ui-button type="secondary" @click="add_kurs" title="Kurs hinzufügen">
					Kurs <i-ri-add-line />
				</svws-ui-button>
			</div>
			<svws-ui-button size="small" type="secondary" @click="splitKurs(kurs)">Aufteilen</svws-ui-button>
			<div>
				<svws-ui-multi-select v-if="kurseMitKursart.size()"
					:model-value="undefined" @update:model-value="combineKurs(kurs, $event)"
					title="Zusammenlegen mit…" class="placeholder--visible text-sm" headless
					:item-text="item => get_kursbezeichnung(item.id)" :items="andereKurse.values()" />
			</div>
			<div class="flex items-center gap-1 ml-auto">
				<div class="mr-1 text-sm">Schienen:</div>
				<svws-ui-button type="icon" @click="del_schiene" size="small">
					<i-ri-subtract-line />
				</svws-ui-button>
				<div class="mx-1">{{ kurs.anzahlSchienen }}</div>
				<svws-ui-button type="icon" @click="add_schiene" size="small">
					<i-ri-add-line />
				</svws-ui-button>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { GostBlockungKurs, GostBlockungKursLehrer, GostBlockungRegel, GostBlockungsdatenManager, GostBlockungsergebnisKurs, LehrerListeEintrag, List } from "@core";
	import type { ComputedRef } from 'vue';
	import { computed } from 'vue';

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
		addSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
		removeSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
		addKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
		removeKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
		combineKurs: (kurs1 : GostBlockungKurs, fach2: GostBlockungKurs | GostBlockungsergebnisKurs) => Promise<void>;
		splitKurs: (kurs: GostBlockungKurs) => Promise<void>;
		addKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<GostBlockungKursLehrer | undefined>;
		removeKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<void>;
		anzahlSpalten: number;
		kurs: GostBlockungKurs;
		kurseMitKursart: List<GostBlockungsergebnisKurs>;
		mapLehrer: Map<number, LehrerListeEintrag>;
		bgColor: string;
	}>();

	const andereKurse: ComputedRef<Map<number, GostBlockungsergebnisKurs>> = computed(() => {
		const result = new Map<number, GostBlockungsergebnisKurs>();
		for (const k of props.kurseMitKursart)
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
