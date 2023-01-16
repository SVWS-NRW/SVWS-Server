<template>
	<div>
		<div class="flex justify-between items-center" :class="{'mb-2' : regeln.length}">
			<h5 class="text-sm font-bold leading-loose pr-4 py-1">
				{{ regelTyp.bezeichnung }}
			</h5>
			<svws-ui-button v-if="!regel && allow_regeln" size="small" type="primary" class="cursor-pointer" @click="regel_hinzufuegen">
				Regel hinzufügen
			</svws-ui-button>
		</div>
		<div v-for="r in regeln" :key="r.id" class="flex justify-between">
			<div class="cursor-pointer" :class="{'bg-dark-20 font-bold px-1 rounded -ml-1':r===regel}" @click="select_regel(r)">
				<slot name="beschreibung" :regel="r" />
			</div>
			<svws-ui-icon v-if="allow_regeln" type="error" class="cursor-pointer" @click="regel_entfernen(r)">
				<i-ri-delete-bin-2-line />
			</svws-ui-icon>
		</div>
		<div v-if="regel && allow_regeln" class="mt-3">
			<div class="inline-flex items-center gap-2 w-full">
				<slot />
				<svws-ui-button type="icon" class="hover--error m1-auto" @click="regel=undefined">
					<svws-ui-icon> <i-ri-delete-bin-2-line /> </svws-ui-icon>
				</svws-ui-button>
				<svws-ui-button type="primary" @click="speichern">
					<svws-ui-icon> <i-ri-check-line /> </svws-ui-icon>
				</svws-ui-button>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { GostBlockungRegel, GostKursblockungRegelTyp } from '@svws-nrw/svws-core-ts';
	import { ComputedRef, computed } from 'vue';
	import { DataGostKursblockung } from '~/apps/gost/DataGostKursblockung';
	import { useVModel } from '~/utils/composables/vmodel';

	const props = defineProps<{
		blockung: DataGostKursblockung;
		modelValue: GostBlockungRegel | undefined;
		regelTyp: GostKursblockungRegelTyp;
	}>()

	const emit = defineEmits<{
		(e: 'update:modelValue', v: GostBlockungRegel | undefined): void,
		(e: 'regelHinzugefuegt', v: GostBlockungRegel): void
	}>()

	const allow_regeln: ComputedRef<boolean> = computed(() => props.blockung.daten?.ergebnisse.size() === 1)

	const regel = useVModel(props, 'modelValue', emit)

	const regeln: ComputedRef<GostBlockungRegel[]> = computed(() => {
		const arr = []
		const regeln = props.blockung.datenmanager?.getMengeOfRegeln()
		if (!regeln)
			return []
		for (const r of regeln)
			if (r.typ === props.regelTyp.typ)
				arr.push(r)
		return arr;
	})

	function select_regel(r: GostBlockungRegel) {
		regel.value = (props.modelValue === r) ? undefined : r;
	}

	const regel_hinzufuegen = () => {
		const r = new GostBlockungRegel()
		r.typ = props.regelTyp.typ
		emit('regelHinzugefuegt', r)
	}

	const speichern = async () => {
		if (!regel.value)
			return
		await props.blockung.add_blockung_regel(regel.value)
		regel.value = undefined
	}

	const regel_entfernen = async (r: GostBlockungRegel|undefined) => {
		if (r === undefined)
			return;
		await props.blockung.del_blockung_regel(r.id)
		if (r === props.modelValue)
			regel.value = undefined
	}

</script>
