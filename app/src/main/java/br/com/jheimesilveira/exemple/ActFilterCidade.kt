package br.com.jheimesilveira.exemple

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.jheimesilveira.js.filterview.dto.JSGroupFilterModel
import br.com.jheimesilveira.js.filterview.dto.JSItemFilterModel
import kotlinx.android.synthetic.main.act_filter_cidade.*

class ActFilterCidade : AppCompatActivity() {

    val filtroModels = ArrayList<JSGroupFilterModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_filter_cidade)
        initFilterview()
    }

    private fun initFilterview() {
        filterView.showViewitemsSelectedInFilter = false

        var model = JSGroupFilterModel("Estados")
        model.multiple = false
        model.type = JSGroupFilterModel.Type.LINEAR
        model.id = "ES"
        filtroModels.add(model)

        model = JSGroupFilterModel("Cidades")
        model.multiple = true
        model.type = JSGroupFilterModel.Type.LINEAR
        model.id = "CT"
        filtroModels.add(model)

        getEstadosRest()
    }

    private fun getEstadosRest() {

        val dialogProgress = ProgressDialog(this@ActFilterCidade)
        dialogProgress.isIndeterminate = true
        dialogProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialogProgress.setMessage("Pesquisando")
        dialogProgress.show()

        WebService().estados({ estados ->
            filtroModels[0].dataSet = getEstados(estados)
            filterView.loadDataSetFilter(filtroModels)

            filterView.onChangeGroupSelected { index, jsGroupFilterModel ->
                when {
                    index == 0 && jsGroupFilterModel.dataSet.size == 0 -> {
                        getCidadesRest(null, jsGroupFilterModel)
                        return@onChangeGroupSelected
                    }

                    index == 0 && jsGroupFilterModel.dataSet.size > 0 -> {
                        getCidadesRest(jsGroupFilterModel.dataSet[0].value.toString(), jsGroupFilterModel)
                        return@onChangeGroupSelected
                    }
                }
            }
            dialogProgress.dismiss()
        }, {
            dialogProgress.dismiss()
            Toast.makeText(this@ActFilterCidade, "Erro na chamada", Toast.LENGTH_SHORT).show()
        })
    }

    private fun getCidadesRest(estadoId: String?, groupSelected: JSGroupFilterModel) {
        filterView.onShowSwipper(true)
        if (estadoId == null) {
            filtroModels[1].dataSet = ArrayList()
            filterView.loadDataSetFilter(filtroModels)
            filterView.loadDataSetFilterSelected(0) { groupFilter, groupFilterSelected -> groupSelected }
            filterView.onShowSwipper(false)
        } else {
            WebService().cidades(estadoId, { cidades ->
                filtroModels[1].dataSet = getCidades(cidades)
                filterView.loadDataSetFilter(filtroModels)
                filterView.loadDataSetFilterSelected(0) { groupFilter, groupFilterSelected -> groupSelected }
                filterView.onShowSwipper(false)

            },{
                filterView.onShowSwipper(false)
                Toast.makeText(this@ActFilterCidade, "Erro na chamada", Toast.LENGTH_SHORT).show()
            })
        }
    }

    private fun getCidades(cidades: ArrayList<Cidade>): ArrayList<JSItemFilterModel> {
        val model = ArrayList<JSItemFilterModel>()

        cidades.map { cidade -> model.add(JSItemFilterModel(cidade.cidade, cidade.cidade)) }

        return model
    }

    private fun getEstados(estados: ArrayList<Estado>): ArrayList<JSItemFilterModel> {
        val model = ArrayList<JSItemFilterModel>()

        estados.map { estado -> model.add(JSItemFilterModel(estado.estado, estado.id)) }

        return model
    }
}
