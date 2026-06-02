<template>
	<div class="flex flex-col w-full h-full overflow-hidden">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<h2 class="svws-headline">
						<span>Reporting</span>
					</h2>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<div class="page page-flex-row">
			<report-parameters :id-hauptdaten-objekt="-1" create-html-preview show-json />
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { SchuleReportingProps } from "./SSchuleReportingProps";

	const props = defineProps<SchuleReportingProps>();

</script>
