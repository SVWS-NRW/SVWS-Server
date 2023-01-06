<script setup lang="ts">
import { injectMainApp, Main } from "~/apps/Main";
import { GostBlockungSchiene } from "@svws-nrw/svws-core-ts";
import { computed } from "vue";

const main: Main = injectMainApp();
const app = main.apps.gost;

const props = defineProps<{ modelValue: GostBlockungSchiene; }>();
const emit = defineEmits(['update:modelValue'])

const selected = computed({
	get(): GostBlockungSchiene {
		return props.modelValue;
	},
	set(val: GostBlockungSchiene) {
		emit('update:modelValue', val);
	}
});
</script>

<template>
	<svws-ui-multi-select
		v-model="selected"
		:items="app.dataKursblockung.datenmanager?.getMengeOfSchienen() || []"
		:item-text="(i: GostBlockungSchiene) => i.bezeichnung.toString()"
		/>
</template>