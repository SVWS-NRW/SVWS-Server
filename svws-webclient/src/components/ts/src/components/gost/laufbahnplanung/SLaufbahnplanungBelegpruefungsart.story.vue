<template>
	<Story title="GostBelegpruefungsarten">
		<Variant title="GostBelegpruefungsArt">
			<s-laufbahnplanung-belegpruefungsart v-model="art" />
		</Variant>
	</Story>
</template>

<script setup lang="ts">
	import type { ShallowRef } from 'vue';
	import { shallowRef } from 'vue';

	const art: ShallowRef<'gesamt'> = shallowRef('gesamt');
</script>
