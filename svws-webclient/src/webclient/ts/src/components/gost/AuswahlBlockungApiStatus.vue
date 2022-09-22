<template>
	<tr v-if="status_blockung" class="table--row table--cell-padded">
		<template v-if="status_blockung.idle">
			<td class="loading-disclaimer" colspan="3">
				<div class="loading-display">
					<span>Blockung wird berechnet...</span>
					<span class="loading-spinner-dimensions">
						<img src="../../../public/loading_spinner.svg" alt="Ladeanzeige"
							class="loading-spinner-dimensions loading-rotation">
					</span>
				</div>
			</td>
		</template>
		<template v-if="status_blockung.error">
			<td class="table--cell table--cell-padded table--border px-8" colspan="3">
				<p class="api-error-text">Fehler beim Berechnen der Blockung!</p>
			</td>
		</template>
	</tr>
</template>

<script setup lang="ts">
import { GostBlockungListeneintrag } from "@svws-nrw/svws-core-ts";
import {
		computed,
		ComputedRef,
	} from "vue";
	import { ApiStatus, FeedbackValues } from "~/apps/gost/userfeedback";
	import { injectMainApp, Main } from "~/apps/Main";
	
	const main: Main = injectMainApp();
	const app = main.apps.gost;
	const {blockung} = defineProps({
		blockung: {
			type: Object as () => GostBlockungListeneintrag,
			required: true
		}
	});
	// const apiStatus: ComputedRef<ApiStatus> = computed(()=>app.apiStatus)
	const status_blockung: ComputedRef<FeedbackValues> = computed(()=>app.getApiStatus(blockung.hashCode()))

	</script>
	
	<style>
	.loading-disclaimer {
		background-color: rgba(var(--svws-ui-color-dark-20), var(--tw-border-opacity));
		--tw-bg-opacity: 1;
		--tw-border-opacity: 1;
		border-width: 1px;
		padding: .25rem .75rem;
		line-height: 1.125;
	}
	
	.loading-spinner-dimensions {
		height: 1rem;
		width: 1rem;
	}
	
	.loading-display {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
	}
	
	.loading-rotation {
		display: block;
		position: relative;
		-webkit-animation: spin 6s steps(11, end) infinite;
		-moz-animation: spin 6s steps(11, end) infinite;
		animation: spin 6s steps(11, end) infinite;
	}
	
	.api-error-text {
		color: rgb(var(--svws-ui-color-error));
		font-weight: 700;
	}
	
	@-webkit-keyframes spin {
		0% {
			-webkit-transform: rotate(0deg);
		}
	
		100% {
			-webkit-transform: rotate(360deg);
		}
	}
	
	@-moz-keyframes spin {
		0% {
			-webkit-transform: rotate(0deg);
		}
	
		100% {
			-webkit-transform: rotate(360deg);
		}
	}
	
	@keyframes spin {
		0% {
			-webkit-transform: rotate(0deg);
		}
	
		100% {
			-webkit-transform: rotate(360deg);
		}
	}
	</style>