<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper>
				<svws-ui-text-input placeholder="Bezeichnung" class="contentFocusField"
					v-model="model.proxy.bezeichnung"
					:validation="() => model.getFehler('bezeichnung')"
					@commit="model.patch"
					:max-len="255" required />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { LernplattformenDatenProps } from "~/components/schule/kataloge/lernplattformen/daten/LernplattformenDatenProps";
	import { LernplattformenModelProxy } from "~/components/schule/kataloge/lernplattformen/modelproxy/LernplattformenModelProxy";

	const props = defineProps<LernplattformenDatenProps>();
	const model = new LernplattformenModelProxy(() => props.manager().daten(), props.manager, props.patch);


</script>
