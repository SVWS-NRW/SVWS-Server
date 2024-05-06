<template>
	<svws-ui-content-card title="Allgemein">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Bezeichnung" :model-value="data.bezeichnung" @change="bezeichnung=>patch({bezeichnung})" type="text" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
	<svws-ui-content-card>
		<template #title>
			<div class="content-card--header content-card--headline text-headline-sm">
			</div>
		</template>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { VermerkartEintrag } from "@core";
	import { computed } from "vue";

	const props = defineProps<{
		data: VermerkartEintrag;
		patch: (data : Partial<VermerkartEintrag>) => Promise<void>;
	}>();
</script>
