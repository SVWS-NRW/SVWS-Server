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
		<div class="page page-grid-cards">
			<svws-ui-content-card title="Reporting">
				<svws-ui-input-wrapper>
					<report-parameters :create-report :id-hauptdaten-objekt="-1" :server-mode="ServerMode.DEV" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { SchuleReportingProps } from "./SSchuleReportingProps";
	import { ServerMode } from "@core";

	const props = defineProps<SchuleReportingProps>();

</script>
