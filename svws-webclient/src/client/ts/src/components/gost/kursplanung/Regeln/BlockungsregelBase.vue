<template>
	<div class="px-7 3xl:px-8" :class="{'py-4 bg-light': modelValue?.typ === regelTyp.typ}">
		<div class="flex justify-between items-start flex-wrap gap-x-4 gap-y-1" :class="{'pb-4' : regeln.length && modelValue?.typ === regelTyp.typ, 'bg-white rounded-t-lg px-3 pt-4 pb-2 -mx-3': modelValue?.typ === regelTyp.typ}">
			<span class="text-headline-sm w-1/2">
				{{ regelTyp.bezeichnung }}
			</span>
			<svws-ui-button v-if="modelValue?.typ !== regelTyp.typ" @click="regel_hinzufuegen" type="transparent" size="small">
				Regel hinzufügen <i-ri-add-circle-line class="-mr-1" />
			</svws-ui-button>
		</div>
		<div v-for="r in regeln" :key="r.id" class="flex justify-between last:pb-3" :class="{'bg-white px-3 py-0.5 -mx-3': modelValue?.typ === regelTyp.typ}">
			<div class="cursor-pointer" :class="{'text-primary font-bold':r===modelValue}" @click="select_regel(r)">
				<slot name="beschreibung" :regel="r" />
			</div>
			<svws-ui-button type="trash" @click="regel_entfernen(r)" />
		</div>
		<div v-if="modelValue?.typ === regelTyp.typ" :class="{'bg-white rounded-b-lg px-3 py-3 -mx-3': modelValue?.typ === regelTyp.typ}">
			<div class="inline-flex items-center gap-2 w-full">
				<slot />
			</div>
			<div class="flex flex-wrap gap-2 mt-8">
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

	import type { GostKursblockungRegelTyp } from "@svws-nrw/svws-core";
	import { GostBlockungRegel } from "@svws-nrw/svws-core";

	const props = defineProps<{
		modelValue: GostBlockungRegel | undefined;
		regelTyp: GostKursblockungRegelTyp;
		regeln: GostBlockungRegel[];
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
