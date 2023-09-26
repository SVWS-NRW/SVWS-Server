<template>
	<div class="pl-5 pr-3 bg-light rounded-lg mb-3">
		<div class="flex justify-between items-start flex-wrap gap-x-4 gap-y-1 rounded-t-lg px-3 pt-4 pb-3 -mx-3" :class="{'rounded-b-lg': !regeln.length}">
			<span class="text-headline-sm w-1/2">
				{{ regelTyp.bezeichnung }}
			</span>
			<svws-ui-button @click="regel_hinzufuegen" type="secondary" size="small" :disabled="modelValue?.typ === regelTyp.typ" class="-mt-0.5" v-if="!disabled">
				Regel <i-ri-add-circle-line class="-mr-1" />
			</svws-ui-button>
		</div>
		<div v-for="r in regeln" :key="r.id" class="flex justify-between last:pb-5 px-3 py-0.5 -mx-3" :class="{'rounded-b-lg': modelValue?.typ !== regelTyp.typ}">
			<div class="cursor-pointer" :class="{'text-primary font-bold':r===modelValue}" @click="select_regel(r)">
				<slot name="beschreibung" :regel="r" />
			</div>
			<svws-ui-button type="trash" @click="regel_entfernen(r)" v-if="!disabled" />
		</div>
		<div v-if="modelValue?.typ === regelTyp.typ && !disabled" class="px-3 pt-5 pb-3 -mx-3">
			<div class="inline-flex items-center gap-2 w-full">
				<slot />
			</div>
			<div class="flex flex-wrap gap-2 mt-5">
				<svws-ui-button type="secondary" @click="emit('update:modelValue', undefined)">
					Abbrechen
				</svws-ui-button>
				<svws-ui-button @click="speichern">
					Speichern
				</svws-ui-button>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { GostKursblockungRegelTyp } from "@core";
	import { GostBlockungRegel } from "@core";

	const props = defineProps<{
		modelValue: GostBlockungRegel | undefined;
		regelTyp: GostKursblockungRegelTyp;
		regeln: GostBlockungRegel[];
		disabled: boolean;
	}>()

	const emit = defineEmits<{
		(e: 'update:modelValue', v: GostBlockungRegel | undefined): void;
		(e: 'regelHinzugefuegen', v: GostBlockungRegel): void;
		(e: 'regelSpeichern'): void;
		(e: 'regelEntfernen', v: GostBlockungRegel): void;
	}>()

	function select_regel(r: GostBlockungRegel) {
		emit('update:modelValue', r);
	}

	const regel_hinzufuegen = () => {
		const r = new GostBlockungRegel()
		r.typ = props.regelTyp.typ
		emit('regelHinzugefuegen', r)
	}

	const speichern = async () => {
		if (!props.modelValue)
			return;
		emit('regelSpeichern');
	}

	const regel_entfernen = async (r: GostBlockungRegel) => {
		emit('regelEntfernen', r)
	}

</script>
