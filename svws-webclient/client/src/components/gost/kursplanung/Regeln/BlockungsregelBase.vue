<template>
	<template v-if="regeln.length || modelValue?.typ === regelTyp.typ">
		<div class="svws-ui-table svws-clickable">
			<div class="svws-ui-thead">
				<div class="svws-ui-tr">
					<div class="svws-ui-td">
						{{ regelTyp.bezeichnung }}
					</div>
				</div>
			</div>
			<div class="svws-ui-tbody">
				<div v-for="r in regeln" :key="r.id" class="svws-ui-tr" :class="{'svws-clicked': modelValue?.id === r.id}">
					<div class="svws-ui-td">
						<div class="flex-grow cursor-pointer inline-flex gap-1 -my-0.5" @click="select_regel(r)">
							<span class="line-clamp-1 break-all leading-tight"><slot name="beschreibung" :regel="r" /></span>
						</div>
						<svws-ui-button type="icon" class="-my-0.5" @click="regel_entfernen(r)" v-if="!disabled"><i-ri-delete-bin-line /></svws-ui-button>
					</div>
				</div>
				<div v-if="modelValue?.typ === regelTyp.typ && !disabled" class="px-3 pt-5 pb-3">
					<div class="inline-flex items-center gap-2 w-full">
						<slot />
					</div>
					<div class="flex justify-end mt-4 gap-2">
						<svws-ui-button type="secondary" @click="emit('update:modelValue', undefined)">
							Abbrechen
						</svws-ui-button>
						<svws-ui-button @click="speichern">
							Speichern
						</svws-ui-button>
					</div>
				</div>
				<div class="svws-ui-tr hover:!bg-white" v-if="modelValue?.typ !== regelTyp.typ">
					<div class="svws-ui-td justify-end my-0.5">
						<svws-ui-button @click="regel_hinzufuegen" type="icon" :disabled="modelValue?.typ === regelTyp.typ" v-if="!disabled">
							<i-ri-add-line />
						</svws-ui-button>
					</div>
				</div>
			</div>
		</div>
	</template>
	<template v-else>
		<div class="svws-initialize -ml-3 my-3">
			<svws-ui-button @click="regel_hinzufuegen" type="transparent" :disabled="modelValue?.typ === regelTyp.typ" v-if="!disabled">
				<i-ri-add-line class="-mr-0.5 -ml-1" /> {{ regelTyp.bezeichnung }}
			</svws-ui-button>
		</div>
	</template>
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

<style lang="postcss" scoped>
.svws-ui-table + .svws-initialize,
.svws-initialize + .svws-ui-table {
  @apply mt-10;
}
</style>
