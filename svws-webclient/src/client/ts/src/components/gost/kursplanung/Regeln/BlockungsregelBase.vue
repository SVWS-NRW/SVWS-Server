<template>
	<div>
		<div class="flex justify-between items-center" :class="{'mb-2' : regeln.length}">
			<h5 class="text-sm font-bold leading-loose pr-4 py-1">
				{{ regelTyp.bezeichnung }}
			</h5>
			<svws-ui-button v-if="modelValue?.typ !== regelTyp.typ" size="small" type="primary" class="cursor-pointer" @click="regel_hinzufuegen">
				Regel hinzufügen
			</svws-ui-button>
		</div>
		<div v-for="r in regeln" :key="r.id" class="flex justify-between">
			<div class="cursor-pointer" :class="{'bg-dark-20 font-bold px-1 rounded -ml-1':r===modelValue}" @click="select_regel(r)">
				<slot name="beschreibung" :regel="r" />
			</div>
			<svws-ui-icon type="error" class="cursor-pointer" @click="regel_entfernen(r)">
				<i-ri-delete-bin-2-line />
			</svws-ui-icon>
		</div>
		<div v-if="modelValue?.typ === regelTyp.typ" class="mt-3">
			<div class="inline-flex items-center gap-2 w-full">
				<slot />
				<svws-ui-button type="icon" class="hover--error m1-auto" @click="emit('update:modelValue', undefined)">
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
